package chocan.reports;

import chocan.data.ProviderRepository;
import chocan.entity.Provider;
import chocan.entity.ProviderDirectory;
import chocan.entity.ServiceRecord;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Summary report for the manager (accounts payable): every provider to be paid
 * this week, their consultations and total fee, and the overall totals.
 *
 * Author: Trey Pierce
 */
public class SummaryReport extends ReportFile {
    private static final String ROW = "%-25s  %-9s  %13s  %11s%n";
    private final Map<String, List<ServiceRecord>> byProvider;
    private final ProviderRepository providerRepo;

    public SummaryReport(Map<String, List<ServiceRecord>> byProvider, ProviderRepository providerRepo,
                         Path reportsDir) {
        super(reportsDir.resolve("manager").resolve("SummaryReport_" + LocalDate.now().format(DATE) + ".txt"));

        this.byProvider = byProvider;
        this.providerRepo = providerRepo;
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder("CHOCAN SUMMARY REPORT (ACCOUNTS PAYABLE)\n");

        sb.append(weekLine());
        sb.append(String.format(ROW, "Provider", "Number", "Consultations", "Total Fee"));
        sb.append(String.format(ROW, dashes(25), dashes(9), dashes(13), dashes(11)));
        int totalConsultations = 0;
        double overallFee = 0;
        
        for (Map.Entry<String, List<ServiceRecord>> e : byProvider.entrySet()) {
            Provider p = providerRepo.findProvider(e.getKey());
            double fee = 0;
        
            for (ServiceRecord r : e.getValue()) {
                fee += r.getFee();
            }
            
            sb.append(String.format(ROW, p == null ? "(deleted provider)" : p.getName(), e.getKey(),
                    e.getValue().size(), ProviderDirectory.money(fee)));
            
            totalConsultations += e.getValue().size();
            overallFee += fee;
        }
        
        sb.append(String.format("%nProviders to be paid:  %d%n", byProvider.size()));
        sb.append(String.format("Total consultations:   %d%n", totalConsultations));
        sb.append(String.format("Overall fee total:     %s%n", ProviderDirectory.money(overallFee)));
        
        return sb.toString();
    }
}
