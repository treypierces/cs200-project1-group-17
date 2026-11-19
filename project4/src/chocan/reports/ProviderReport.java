package chocan.reports;

import chocan.data.MemberRepository;
import chocan.entity.Member;
import chocan.entity.Provider;
import chocan.entity.ProviderDirectory;
import chocan.entity.ServiceRecord;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Weekly provider report: provider details, each service billed, the number of
 * consultations, and the total fee for the week.
 *
 * Author: Trey Pierce
 */
public class ProviderReport extends ReportFile {
    private static final String ROW = "%-12s  %-19s  %-25s  %-9s  %-6s  %9s%n";
    private final Provider provider;
    private final List<ServiceRecord> services;
    private final MemberRepository memberRepo;
    private final int totalConsultations;
    private final double totalFee;

    public ProviderReport(Provider provider, List<ServiceRecord> services, MemberRepository memberRepo,
                          Path reportsDir) {
        super(reportsDir.resolve("providers").resolve(nameForFile(provider.getName())));

        this.provider = provider;
        this.services = new ArrayList<>(services);
        this.services.sort(Comparator.comparing(ServiceRecord::getServiceDate)
                .thenComparing(ServiceRecord::getReceivedDateTime));
        this.memberRepo = memberRepo;
        this.totalConsultations = services.size();
        double sum = 0;
        
        for (ServiceRecord r : services) {
            sum += r.getFee();
        }
        
        this.totalFee = sum;
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder("CHOCAN PROVIDER REPORT\n");

        sb.append(weekLine());
        sb.append(personBlock("Provider", provider));
        sb.append(String.format(ROW, "Service Date", "Received", "Member Name", "Member #", "Code", "Fee"));
        sb.append(String.format(ROW, dashes(12), dashes(19), dashes(25), dashes(9), dashes(6), dashes(9)));
        
        for (ServiceRecord r : services) {
            Member m = memberRepo.findMember(r.getMemberNumber());
        
            sb.append(String.format(ROW, r.getServiceDate().format(DATE),
                    r.getReceivedDateTime().format(DATE_TIME),
                    m == null ? "(deleted member)" : m.getName(),
                    r.getMemberNumber(), r.getServiceCode(), ProviderDirectory.money(r.getFee())));
        }
        
        sb.append(String.format("%nTotal consultations: %d%n", totalConsultations));
        sb.append(String.format("Total fee for week:  %s%n", ProviderDirectory.money(totalFee)));
        
        return sb.toString();
    }
}
