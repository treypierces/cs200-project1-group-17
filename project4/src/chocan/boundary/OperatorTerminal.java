package chocan.boundary;

import chocan.control.MemberController;
import chocan.control.ProviderController;
import chocan.control.VerifyController;
import chocan.entity.Address;
import chocan.entity.Member;
import chocan.entity.Person;
import chocan.entity.Provider;

import java.util.Scanner;

/**
 * Operator terminal: add, update, delete, and list member and provider records.
 * Data is checked against Appendix A field sizes, and updates and deletes
 * must be confirmed.
 *
 * Author: Adam Davis (member records), Samuel Harvey (provider records)
 */
public class OperatorTerminal extends Terminal {
    private final VerifyController verifier;
    private final MemberController memberCtl;
    private final ProviderController providerCtl;

    public OperatorTerminal(Scanner scanner, VerifyController verifier, MemberController memberCtl,
                            ProviderController providerCtl) {
        super(scanner);
        this.verifier = verifier;
        this.memberCtl = memberCtl;
        this.providerCtl = providerCtl;
    }

    public boolean verifyOperator(String operatorNumber) {
        return verifier.verifyOperator(operatorNumber);
    }

    @Override
    protected boolean verify(String number) { return verifyOperator(number); }

    @Override
    protected String welcomeName(String number) { return verifier.getUserName(number); }

    @Override
    public void showMainMenu() {
        while (true) {
            display("");
            display("--- Operator Terminal (" + welcomeName(userNumber) + ") ---");
            display("1. Add member");
            display("2. Update member");
            display("3. Delete member");
            display("4. List members");
            display("5. Add provider");
            display("6. Update provider");
            display("7. Delete provider");
            display("8. List providers");
            display("0. Log out");
            int choice = readChoice("Choose an option: ", 0, 8);
            switch (choice) {
                case 1: addMember(); break;
                case 2: updateMember(readInput("Member number: ")); break;
                case 3: deleteMember(readInput("Member number: ")); break;
                case 4: listMembers(); break;
                case 5: addProvider(); break;
                case 6: updateProvider(readInput("Provider number: ")); break;
                case 7: deleteProvider(readInput("Provider number: ")); break;
                case 8: listProviders(); break;
                default:
                    display("Logged out.");
                    return;
            }
        }
    }

    // ----- Member records (Adam Davis) -----

    public void addMember() {
        display("Add member. Enter the member's information.");
        Object[] data = enterData(null, true);
        String number = memberCtl.addMember((String) data[0], (Address) data[1]);
        display("Member added. Member number: " + number + " (status ACTIVE)");
    }

    public void updateMember(String memberNumber) {
        Member member = memberCtl.getMember(memberNumber);
        if (member == null) {
            display("Not found.");
            return;
        }
        display("Current: " + member);
        display("Enter new member data. Press Enter to keep the value in brackets.");
        Object[] data = enterData(member, true);
        String status = readInput("Fees paid? Status A = ACTIVE, S = SUSPENDED [" + member.getStatus().charAt(0) + "]: ");
        if (!confirm("Save these changes?")) {
            display("Update cancelled.");
            return;
        }
        memberCtl.updateMember(memberNumber, (String) data[0], (Address) data[1]);
        if (status.equalsIgnoreCase("A") || status.equalsIgnoreCase("S")) {
            memberCtl.updateMemberStatus(memberNumber, status.equalsIgnoreCase("A"));
        }
        display("Member updated: " + memberCtl.getMember(memberNumber));
    }

    public void deleteMember(String memberNumber) {
        Member member = memberCtl.getMember(memberNumber);
        if (member == null) {
            display("Not found.");
            return;
        }
        display(member.toString());
        if (confirm("Delete this member?") && memberCtl.deleteMember(memberNumber)) {
            display("Member deleted.");
        } else {
            display("Delete cancelled.");
        }
    }

    public void listMembers() {
        display("Number     Name                       Status     Address");
        for (Member m : memberCtl.listMembers()) {
            display(m.toString());
        }
    }

    // ----- Provider records (Samuel Harvey) -----

    public void addProvider() {
        display("Add provider. Enter the provider's information.");
        Object[] data = enterData(null, false);
        String number = providerCtl.addProvider((String) data[0], (Address) data[1]);
        display("Provider added. Provider number: " + number);
    }

    public void updateProvider(String providerNumber) {
        Provider provider = providerCtl.getProvider(providerNumber);
        if (provider == null) {
            display("Not found.");
            return;
        }
        display("Current: " + provider);
        display("Enter new provider data. Press Enter to keep the value in brackets.");
        Object[] data = enterData(provider, false);
        if (!confirm("Save these changes?")) {
            display("Update cancelled.");
            return;
        }
        providerCtl.updateProvider(providerNumber, (String) data[0], (Address) data[1]);
        display("Provider updated: " + providerCtl.getProvider(providerNumber));
    }

    public void deleteProvider(String providerNumber) {
        Provider provider = providerCtl.getProvider(providerNumber);
        if (provider == null) {
            display("Not found.");
            return;
        }
        display(provider.toString());
        if (confirm("Delete this provider?") && providerCtl.deleteProvider(providerNumber)) {
            display("Provider deleted.");
        } else {
            display("Delete cancelled.");
        }
    }

    public void listProviders() {
        display("Number     Name                       Address");
        for (Provider p : providerCtl.listProviders()) {
            display(p.toString());
        }
    }

    // ----- Shared data entry -----

    /**
     * Reads a name and address. Pressing Enter keeps the value in brackets. The data is
     * checked by the controller's validateData(); if anything is wrong, the problems are
     * shown and the operator re-enters the data. Returns {name, address}.
     */
    private Object[] enterData(Person current, boolean isMember) {
        String name = current == null ? null : current.getName();
        Address a = current == null ? null : current.getAddress();
        String street = a == null ? null : a.getStreet();
        String city = a == null ? null : a.getCity();
        String state = a == null ? null : a.getState();
        String zip = a == null ? null : a.getZip();
        while (true) {
            name = field("Name (max 25)", name);
            street = field("Street address (max 25)", street);
            city = field("City (max 14)", city);
            state = field("State (2 letters)", state).toUpperCase();
            zip = field("ZIP code (5 digits)", zip);
            Address address = new Address(street, city, state, zip);
            String problems = isMember ? memberCtl.validateData(name, address)
                                       : providerCtl.validateData(name, address);
            if (problems.isEmpty()) {
                return new Object[] {name, address};
            }
            display("  " + problems);
            display("  Please re-enter the data. Press Enter to keep a value.");
        }
    }

    private String field(String label, String current) {
        String value = readInput(current == null ? label + ": " : label + " [" + current + "]: ");
        return value.isEmpty() && current != null ? current : value;
    }
}
