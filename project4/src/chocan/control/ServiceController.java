package chocan.control;

import chocan.data.ServiceRecordRepository;
import chocan.entity.ProviderDirectory;
import chocan.entity.Service;
import chocan.entity.ServiceRecord;

import java.time.LocalDate;

/**
 * Billing: looks up service codes in the Provider Directory, saves service
 * records, and writes the directory file for providers.
 *
 * Author: Richard Norman
 */
public class ServiceController {
    private final ProviderDirectory directory;
    private final ServiceRecordRepository recordRepo;

    public ServiceController(ProviderDirectory directory, ServiceRecordRepository recordRepo) {
        this.directory = directory;
        this.recordRepo = recordRepo;
    }

    /** Returns the service name for a code, or null if the code is not in the directory. */
    public String getServiceName(String serviceCode) {
        Service s = directory.findService(serviceCode);
        return s == null ? null : s.getName();
    }

    /** Saves a service record and returns the fee to be paid. */
    public double billService(String memberNumber, String providerNumber, LocalDate serviceDate,
                              String serviceCode, String comments) {
        double fee = directory.getFee(serviceCode);
        ServiceRecord record = new ServiceRecord(serviceDate, providerNumber, memberNumber,
                serviceCode, fee, comments);
        recordRepo.addRecord(record);
        return fee;
    }

    /** Sorts the directory by name and writes it to a file. Returns true if the file was written. */
    public boolean requestProviderDirectory() {
        directory.sortByName();
        return directory.writeFile();
    }

    public String getDirectoryListing() {
        directory.sortByName();
        return directory.getListing();
    }

    public String getDirectoryFileName() {
        return directory.getFileName().toString();
    }
}
