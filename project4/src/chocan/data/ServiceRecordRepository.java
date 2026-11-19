package chocan.data;

import chocan.entity.ServiceRecord;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores every service record and saves them to data/records.txt.
 *
 * Author: Richard Norman
 */
public class ServiceRecordRepository {
    private final Path fileName;
    private final List<ServiceRecord> records = new ArrayList<>();

    public ServiceRecordRepository(Path dataDir) {
        this.fileName = dataDir.resolve("records.txt");
        load();
    }

    public boolean addRecord(ServiceRecord record) {
        records.add(record);
        return save();
    }

    /** Returns the records received in the last seven days, including today. */
    public List<ServiceRecord> getWeekRecords() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        List<ServiceRecord> week = new ArrayList<>();
        for (ServiceRecord r : records) {
            LocalDate received = r.getReceivedDateTime().toLocalDate();
            if (!received.isBefore(weekStart) && !received.isAfter(today)) {
                week.add(r);
            }
        }
        return week;
    }

    private void load() {
        if (!DataFile.exists(fileName)) {
            records.addAll(SeedData.records());
            save();
            return;
        }
        for (String[] f : DataFile.readRows(fileName)) {
            records.add(new ServiceRecord(LocalDateTime.parse(f[0]), LocalDate.parse(f[1]),
                    f[2], f[3], f[4], Double.parseDouble(f[5]), f[6]));
        }
    }

    private boolean save() {
        List<String> lines = new ArrayList<>();
        for (ServiceRecord r : records) {
            lines.add(DataFile.join(r.getReceivedDateTime().toString(), r.getServiceDate().toString(),
                    r.getProviderNumber(), r.getMemberNumber(), r.getServiceCode(),
                    String.format("%.2f", r.getFee()), r.getComments()));
        }
        return DataFile.writeRows(fileName, lines);
    }
}
