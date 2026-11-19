package chocan.control;

import chocan.data.MemberRepository;
import chocan.data.ProviderRepository;
import chocan.data.ServiceRecordRepository;
import chocan.entity.Member;
import chocan.entity.Provider;
import chocan.entity.ProviderDirectory;
import chocan.entity.ServiceRecord;
import chocan.reports.EFTRecord;
import chocan.reports.MemberReport;
import chocan.reports.ProviderReport;
import chocan.reports.ReportFile;
import chocan.reports.SummaryReport;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds and writes the weekly reports from the week's service records.
 * runWeeklyAccounting() is the main accounting procedure (Friday 12:00 AM);
 * the manager can also request each report on its own.
 *
 * Author: Trey Pierce
 */
public class ReportController {
    private final ServiceRecordRepository recordRepo;
    private final MemberRepository memberRepo;
    private final ProviderRepository providerRepo;
    private final ProviderDirectory directory;
    private final Path reportsDir;

    public ReportController(ServiceRecordRepository recordRepo, MemberRepository memberRepo,
                            ProviderRepository providerRepo, ProviderDirectory directory, Path reportsDir) {
        this.recordRepo = recordRepo;
        this.memberRepo = memberRepo;
        this.providerRepo = providerRepo;
        this.directory = directory;
        this.reportsDir = reportsDir;
    }

    /** Main accounting procedure: member reports, provider reports, summary report, and EFT data. */
    public List<ReportFile> runWeeklyAccounting() {
        List<ReportFile> all = new ArrayList<>();

        all.addAll(generateMemberReport());
        all.addAll(generateProviderReport());
        all.add(generateSummaryReport());
        all.add(generateEFTRecord());

        return all;
    }

    /** Writes one report for each member who received a service this week. */
    public List<ReportFile> generateMemberReport() {
        List<ReportFile> written = new ArrayList<>();

        for (Map.Entry<String, List<ServiceRecord>> e : groupByMember(recordRepo.getWeekRecords()).entrySet()) {
            Member member = memberRepo.findMember(e.getKey());

            if (member != null) {
                ReportFile report = new MemberReport(member, e.getValue(), providerRepo, directory, reportsDir);

                report.writeFile();
                written.add(report);
            }
        }

        return written;
    }

    /** Writes one report for each provider who billed ChocAn this week. */
    public List<ReportFile> generateProviderReport() {
        List<ReportFile> written = new ArrayList<>();

        for (Map.Entry<String, List<ServiceRecord>> e : groupByProvider(recordRepo.getWeekRecords()).entrySet()) {
            Provider provider = providerRepo.findProvider(e.getKey());

            if (provider != null) {
                ReportFile report = new ProviderReport(provider, e.getValue(), memberRepo, reportsDir);

                report.writeFile();
                written.add(report);
            }
        }

        return written;
    }

    /** Writes the summary report for the manager (accounts payable). */
    public ReportFile generateSummaryReport() {
        ReportFile report = new SummaryReport(groupByProvider(recordRepo.getWeekRecords()), providerRepo, reportsDir);

        report.writeFile();

        return report;
    }

    /** Writes the EFT data: provider name, number, and amount to transfer. */
    public ReportFile generateEFTRecord() {
        EFTRecord eft = new EFTRecord(reportsDir);

        for (Map.Entry<String, List<ServiceRecord>> e : groupByProvider(recordRepo.getWeekRecords()).entrySet()) {
            Provider provider = providerRepo.findProvider(e.getKey());
            String name = provider == null ? "(deleted provider)" : provider.getName();

            eft.addEntry(name, e.getKey(), totalFee(e.getValue()));
        }

        eft.writeFile();
        
        return eft;
    }

    private Map<String, List<ServiceRecord>> groupByMember(List<ServiceRecord> records) {
        Map<String, List<ServiceRecord>> map = new TreeMap<>();
        
        for (ServiceRecord r : records) {
            map.computeIfAbsent(r.getMemberNumber(), k -> new ArrayList<>()).add(r);
        }
        
        return map;
    }

    private Map<String, List<ServiceRecord>> groupByProvider(List<ServiceRecord> records) {
        Map<String, List<ServiceRecord>> map = new TreeMap<>();

        for (ServiceRecord r : records) {
            map.computeIfAbsent(r.getProviderNumber(), k -> new ArrayList<>()).add(r);
        }
        
        return map;
    }

    private double totalFee(List<ServiceRecord> records) {
        double total = 0;

        for (ServiceRecord r : records) {
            total += r.getFee();
        }
        
        return total;
    }
}
