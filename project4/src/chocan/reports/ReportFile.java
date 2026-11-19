package chocan.reports;

import chocan.data.DataFile;
import chocan.entity.Address;
import chocan.entity.Person;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Base class for every report file. Subclasses build the text; writeFile() saves it.
 *
 * Author: Trey Pierce
 */
public abstract class ReportFile {
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    protected final Path fileName;
    protected final LocalDate reportDate;

    protected ReportFile(Path fileName) {
        this.fileName = fileName;
        this.reportDate = LocalDate.now();
    }

    /** Returns the full text of the report. */
    public abstract String getContent();

    /** Writes the report to its file. Returns true if it was written. */
    public boolean writeFile() {
        return DataFile.writeText(fileName, getContent());
    }

    public Path getFileName() { return fileName; }

    // ----- formatting helpers shared by all reports -----

    /** File names start with the person's name followed by the date, per Appendix A. */
    protected static String nameForFile(String name) {
        return name.replaceAll("[^A-Za-z0-9]+", "_") + "_" + LocalDate.now().format(DATE) + ".txt";
    }

    protected String weekLine() {
        return "Week: " + reportDate.minusDays(6).format(DATE) + " to " + reportDate.format(DATE) + "\n\n";
    }

    protected static String personBlock(String label, Person p) {
        Address a = p.getAddress();

        return String.format("%-16s %s%n", label + " name:", p.getName())
                + String.format("%-16s %s%n", label + " number:", p.getNumber())
                + String.format("%-16s %s%n", "Street address:", a.getStreet())
                + String.format("%-16s %s%n", "City:", a.getCity())
                + String.format("%-16s %s%n", "State:", a.getState())
                + String.format("%-16s %s%n%n", "ZIP code:", a.getZip());
    }

    protected static String dashes(int n) {
        return new String(new char[n]).replace('\0', '-');
    }
}
