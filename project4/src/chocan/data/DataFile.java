package chocan.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the pipe-delimited text files used to save data between sessions.
 *
 * Author: Clark Berry
 */
public final class DataFile {
    private static final String SEP = "|";

    private DataFile() { }

    public static boolean exists(Path file) {
        return Files.exists(file);
    }

    /** Reads every non-blank line and splits it into fields. Returns an empty list if the file is missing. */
    public static List<String[]> readRows(Path file) {
        List<String[]> rows = new ArrayList<>();
        if (!Files.exists(file)) {
            return rows;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split("\\|", -1));
                }
            }
        } catch (IOException e) {
            System.out.println("WARNING: could not read " + file + ": " + e.getMessage());
        }
        return rows;
    }

    /** Overwrites the file with the given lines. Returns false if the write failed. */
    public static boolean writeRows(Path file, List<String> lines) {
        return writeText(file, String.join(System.lineSeparator(), lines) + System.lineSeparator());
    }

    /** Overwrites the file with the given text, creating folders as needed. */
    public static boolean writeText(Path file, String text) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.write(file, text.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            System.out.println("WARNING: could not write " + file + ": " + e.getMessage());
            return false;
        }
    }

    /** Joins fields into one line of a data file. */
    public static String join(String... fields) {
        return String.join(SEP, fields);
    }
}
