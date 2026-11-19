package chocan;

import chocan.boundary.ManagerTerminal;
import chocan.boundary.OperatorTerminal;
import chocan.boundary.ProviderTerminal;
import chocan.control.MemberController;
import chocan.control.ProviderController;
import chocan.control.ReportController;
import chocan.control.ServiceController;
import chocan.control.VerifyController;
import chocan.data.MemberRepository;
import chocan.data.ProviderRepository;
import chocan.data.SeedData;
import chocan.data.ServiceRecordRepository;
import chocan.entity.ProviderDirectory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Starts the ChocAn system: loads saved data (or sample data on first run),
 * connects the classes, and shows the start screen where a user picks a terminal.
 *
 * Usage: java -cp out chocan.Main [--reset]
 *   --reset  deletes saved data and reloads the sample data
 *
 * Author: Clark Berry
 */
public class Main {
    public static void main(String[] args) {
        Path dataDir = Paths.get("data");
        Path reportsDir = Paths.get("reports");
        if (args.length > 0 && args[0].equals("--reset")) {
            File[] files = dataDir.toFile().listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
            System.out.println("Saved data cleared. Sample data loaded.");
        }

        // Data
        MemberRepository memberRepo = new MemberRepository(dataDir);
        ProviderRepository providerRepo = new ProviderRepository(dataDir);
        ServiceRecordRepository recordRepo = new ServiceRecordRepository(dataDir);
        ProviderDirectory directory = new ProviderDirectory(dataDir, reportsDir);

        // Control
        VerifyController verifier = new VerifyController(providerRepo, SeedData.staffAccounts());
        MemberController memberCtl = new MemberController(memberRepo);
        ProviderController providerCtl = new ProviderController(providerRepo);
        ServiceController serviceCtl = new ServiceController(directory, recordRepo);
        ReportController reportCtl = new ReportController(recordRepo, memberRepo, providerRepo, directory, reportsDir);

        // Boundary
        Scanner scanner = new Scanner(System.in);
        ProviderTerminal providerTerminal = new ProviderTerminal(scanner, verifier, memberCtl, serviceCtl);
        OperatorTerminal operatorTerminal = new OperatorTerminal(scanner, verifier, memberCtl, providerCtl);
        ManagerTerminal managerTerminal = new ManagerTerminal(scanner, verifier, reportCtl);

        try {
            while (true) {
                System.out.println();
                System.out.println("===== ChocAn Data Processing System =====");
                System.out.println("1. Provider terminal");
                System.out.println("2. Operator terminal");
                System.out.println("3. Manager terminal");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("1")) {
                    providerTerminal.start("Provider number: ");
                } else if (choice.equals("2")) {
                    operatorTerminal.start("Operator number: ");
                } else if (choice.equals("3")) {
                    managerTerminal.start("Manager number: ");
                } else if (choice.equals("0")) {
                    System.out.println("Goodbye.");
                    return;
                } else {
                    System.out.println("  Please enter a number from 0 to 3.");
                }
            }
        } catch (NoSuchElementException e) {
            // Input ended (for example, the end of a piped test script).
            System.out.println();
            System.out.println("Input closed. Exiting.");
        }
    }
}
