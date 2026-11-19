package chocan.reports;

import chocan.data.ProviderRepository;
import chocan.entity.Member;
import chocan.entity.Provider;
import chocan.entity.ProviderDirectory;
import chocan.entity.Service;
import chocan.entity.ServiceRecord;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Weekly member report: member details, then each service received, sorted by service date.
 *
 * Author: Trey Pierce
 */
public class MemberReport extends ReportFile {
    private final Member member;
    private final List<ServiceRecord> services;
    private final ProviderRepository providerRepo;
    private final ProviderDirectory directory;

    public MemberReport(Member member, List<ServiceRecord> services, ProviderRepository providerRepo,
                        ProviderDirectory directory, Path reportsDir) {
        super(reportsDir.resolve("members").resolve(nameForFile(member.getName())));

        this.member = member;
        this.services = new ArrayList<>(services);
        this.services.sort(Comparator.comparing(ServiceRecord::getServiceDate));
        this.providerRepo = providerRepo;
        this.directory = directory;
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder("CHOCAN MEMBER REPORT\n");

        sb.append(weekLine());
        sb.append(personBlock("Member", member));
        sb.append(String.format("%-15s  %-25s  %s%n", "Date of Service", "Provider", "Service"));
        sb.append(String.format("%-15s  %-25s  %s%n", dashes(15), dashes(25), dashes(20)));
        
        for (ServiceRecord r : services) {
            Provider p = providerRepo.findProvider(r.getProviderNumber());
            Service s = directory.findService(r.getServiceCode());
        
            sb.append(String.format("%-15s  %-25s  %s%n", r.getServiceDate().format(DATE),
                    p == null ? "(deleted provider)" : p.getName(),
                    s == null ? r.getServiceCode() : s.getName()));
        }
        
        return sb.toString();
    }
}
