package chocan.reports;

import chocan.entity.ProviderDirectory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Electronic funds transfer data: one line per provider with the amount to pay.
 *
 * Author: Trey Pierce
 */
public class EFTRecord extends ReportFile {
    private static final String ROW = "%-25s  %-9s  %11s%n";
    private final List<String> entries = new ArrayList<>();

    public EFTRecord(Path reportsDir) {
        super(reportsDir.resolve("eft").resolve("EFT_" + LocalDate.now().format(DATE) + ".txt"));
    }

    public void addEntry(String providerName, String providerNumber, double amount) {
        entries.add(String.format(ROW, providerName, providerNumber, ProviderDirectory.money(amount)));
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder("CHOCAN EFT DATA\n");

        sb.append(weekLine());
        sb.append(String.format(ROW, "Provider Name", "Number", "Amount"));
        sb.append(String.format(ROW, dashes(25), dashes(9), dashes(11)));
        
        for (String line : entries) {
            sb.append(line);
        }
        
        return sb.toString();
    }
}
