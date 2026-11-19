package chocan.entity;

import chocan.data.DataFile;
import chocan.data.SeedData;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Provider Directory: every service ChocAn pays for, with its code and fee.
 * Loaded from data/services.txt. writeFile() saves an alphabetical copy for providers.
 *
 * Author: Samuel Harvey
 */
public class ProviderDirectory {
    private final List<Service> services = new ArrayList<>();
    private final Path dataFile;
    private final Path fileName;

    public ProviderDirectory(Path dataDir, Path reportsDir) {
        this.dataFile = dataDir.resolve("services.txt");
        this.fileName = reportsDir.resolve("directory").resolve("ProviderDirectory.txt");
        load();
    }

    /** Returns the service with this code, or null if the code is not in the directory. */
    public Service findService(String serviceCode) {
        for (Service s : services) {
            if (s.getCode().equals(serviceCode)) {
                return s;
            }
        }
        return null;
    }

    /** Returns the fee for a service code, or 0 if the code is not in the directory. */
    public double getFee(String serviceCode) {
        Service s = findService(serviceCode);
        return s == null ? 0 : s.getFee();
    }

    /** Sorts the services alphabetically by name. */
    public void sortByName() {
        services.sort(Comparator.comparing(Service::getName, String.CASE_INSENSITIVE_ORDER));
    }

    /** Returns the directory as a table of service name, code, and fee. */
    public String getListing() {
        StringBuilder sb = new StringBuilder("CHOCAN PROVIDER DIRECTORY\n\n");
        sb.append(String.format("%-20s  %-6s  %9s%n", "Service Name", "Code", "Fee"));
        sb.append(String.format("%-20s  %-6s  %9s%n", "--------------------", "------", "---------"));
        for (Service s : services) {
            sb.append(String.format("%-20s  %-6s  %9s%n", s.getName(), s.getCode(), money(s.getFee())));
        }
        return sb.toString();
    }

    /** Writes the directory to reports/directory/ProviderDirectory.txt. */
    public boolean writeFile() {
        return DataFile.writeText(fileName, getListing());
    }

    public Path getFileName() { return fileName; }

    private void load() {
        if (!DataFile.exists(dataFile)) {
            services.addAll(SeedData.services());
            List<String> lines = new ArrayList<>();
            for (Service s : services) {
                lines.add(DataFile.join(s.getCode(), s.getName(), String.format("%.2f", s.getFee())));
            }
            DataFile.writeRows(dataFile, lines);
        } else {
            for (String[] f : DataFile.readRows(dataFile)) {
                services.add(new Service(f[0], f[1], Double.parseDouble(f[2])));
            }
        }
    }

    /** Formats a dollar amount, for example $1,234.50. */
    public static String money(double amount) {
        return String.format("$%,.2f", amount);
    }
}
