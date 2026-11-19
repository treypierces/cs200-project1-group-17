package chocan.boundary;

import chocan.control.ReportController;
import chocan.control.VerifyController;
import chocan.reports.ReportFile;

import java.util.List;
import java.util.Scanner;

/**
 * Manager terminal: request the member, provider, or summary report, or run the
 * main accounting procedure now (it normally runs Friday at 12:00 AM).
 *
 * Author: Trey Pierce
 */
public class ManagerTerminal extends Terminal {
    private final VerifyController verifier;
    private final ReportController reportCtl;

    public ManagerTerminal(Scanner scanner, VerifyController verifier, ReportController reportCtl) {
        super(scanner);

        this.verifier = verifier;
        this.reportCtl = reportCtl;
    }

    public boolean verifyManager(String managerNumber) {
        return verifier.verifyManager(managerNumber);
    }

    @Override
    protected boolean verify(String number) { return verifyManager(number); }

    @Override
    protected String welcomeName(String number) { return verifier.getUserName(number); }

    @Override
    public void showMainMenu() {
        while (true) {
            display("");
            display("--- Manager Terminal (" + welcomeName(userNumber) + ") ---");
            display("1. Run main accounting procedure");
            display("2. Member reports");
            display("3. Provider reports");
            display("4. Summary report");
            display("0. Log out");

            int choice = readChoice("Choose an option: ", 0, 4);
            
            switch (choice) {
                case 1: runWeeklyAccounting(); break;
                case 2: requestMemberReport(); break;
                case 3: requestProviderReport(); break;
                case 4: requestSummaryReport(); break;
                default:
                    display("Logged out.");
                    return;
            }
        }
    }

    /** Runs the main accounting procedure and lists every file written. */
    public void runWeeklyAccounting() {
        List<ReportFile> reports = reportCtl.runWeeklyAccounting();

        display("Main accounting procedure complete. Files written:");
        
        ReportFile summary = null;
        
        for (ReportFile r : reports) {
            display("  " + r.getFileName());
            
            if (r.getFileName().toString().contains("SummaryReport")) {
                summary = r;
            }
        }
        
        if (summary != null) {
            display("");
            display(summary.getContent());
        }
    }

    public void requestMemberReport() {
        show(reportCtl.generateMemberReport(), "Member reports written");
    }

    public void requestProviderReport() {
        show(reportCtl.generateProviderReport(), "Provider reports written");
    }

    public void requestSummaryReport() {
        ReportFile report = reportCtl.generateSummaryReport();

        display("");
        display(report.getContent());
        display("Summary report written: " + report.getFileName());
    }

    private void show(List<ReportFile> reports, String doneMessage) {
        if (reports.isEmpty()) {
            display("No services were recorded this week.");

            return;
        }
        
        for (ReportFile r : reports) {
            display("");
            display(r.getContent());
        }
        
        display(doneMessage + " (" + reports.size() + " files):");
        
        for (ReportFile r : reports) {
            display("  " + r.getFileName());
        }
    }
}
