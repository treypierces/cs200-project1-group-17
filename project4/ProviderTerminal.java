package chocan.boundary;

import chocan.control.MemberController;
import chocan.control.ServiceController;
import chocan.control.VerifyController;
import chocan.entity.ProviderDirectory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Provider terminal: validate a member, bill ChocAn for a service, and request
 * the Provider Directory.
 *
 * Author: Richard Norman
 */
public class ProviderTerminal extends Terminal {
    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final VerifyController verifier;
    private final MemberController memberCtl;
    private final ServiceController serviceCtl;

    public ProviderTerminal(Scanner scanner, VerifyController verifier, MemberController memberCtl,
                            ServiceController serviceCtl) {
        super(scanner);
        this.verifier = verifier;
        this.memberCtl = memberCtl;
        this.serviceCtl = serviceCtl;
    }

    public boolean verifyProvider(String providerNumber) {
        return verifier.verifyProvider(providerNumber);
    }

    @Override
    protected boolean verify(String number) { return verifyProvider(number); }

    @Override
    protected String welcomeName(String number) { return verifier.getUserName(number); }

    @Override
    public void showMainMenu() {
        while (true) {
            display("");
            display("--- Provider Terminal (" + welcomeName(userNumber) + ", #" + userNumber + ") ---");
            display("1. Validate member");
            display("2. Bill for service");
            display("3. Request Provider Directory");
            display("0. Log out");
            int choice = readChoice("Choose an option: ", 0, 3);
            if (choice == 1) {
                validateMember(readInput("Member number: "));
            } else if (choice == 2) {
                billForService(readInput("Member number: "));
            } else if (choice == 3) {
                requestProviderDirectory();
            } else {
                display("Logged out.");
                return;
            }
        }
    }

    /** Shows Validated, Invalid Number, or Member Suspended, and returns true if validated. */
    public boolean validateMember(String memberNumber) {
        String result = memberCtl.validateMember(memberNumber);
        display(result);
        return MemberController.VALIDATED.equals(result);
    }

    /** Billing steps from Appendix A. Nothing is billed unless the member is validated first. */
    public void billForService(String memberNumber) {
        if (!validateMember(memberNumber)) {
            return;
        }
        LocalDate serviceDate = readServiceDate();

        String serviceCode;
        while (true) {
            serviceCode = readInput("Service code (6 digits, or D to view the directory): ");
            if (serviceCode.equalsIgnoreCase("D")) {
                display(serviceCtl.getDirectoryListing());
                continue;
            }
            String serviceName = serviceCode.matches("\\d{6}") ? serviceCtl.getServiceName(serviceCode) : null;
            if (serviceName == null) {
                display("  Invalid service code.");
            } else {
                display("Service: " + serviceName);
                if (confirm("Is this the correct service?")) {
                    break;
                }
            }
        }

        String comments;
        while (true) {
            comments = readInput("Comments (optional, max 100 characters): ");
            if (comments.length() <= 100 && !comments.contains("|")) {
                break;
            }
            display("  Comments must be 100 characters or fewer.");
        }

        double fee = serviceCtl.billService(memberNumber, userNumber, serviceDate, serviceCode, comments);
        display("Service recorded for member " + memberNumber + " on " + serviceDate.format(DATE) + ".");
        display("Fee to be paid: " + ProviderDirectory.money(fee));
    }

    /** Writes the directory file and shows it on screen. */
    public void requestProviderDirectory() {
        if (serviceCtl.requestProviderDirectory()) {
            display(serviceCtl.getDirectoryListing());
            display("Provider Directory file created: " + serviceCtl.getDirectoryFileName());
        } else {
            display("Could not create the Provider Directory file.");
        }
    }

    /** Reads the date the service was provided (MM-DD-YYYY, not in the future). */
    private LocalDate readServiceDate() {
        while (true) {
            String text = readInput("Date of service (MM-DD-YYYY): ");
            try {
                LocalDate date = LocalDate.parse(text, DATE);
                if (!date.isAfter(LocalDate.now())) {
                    return date;
                }
            } catch (DateTimeParseException e) {
                // fall through to the error message
            }
            display("  Enter a valid date in MM-DD-YYYY format that is not in the future.");
        }
    }
}
