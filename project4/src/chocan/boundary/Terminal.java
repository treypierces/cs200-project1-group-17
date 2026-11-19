package chocan.boundary;

import java.util.Scanner;

/**
 * Base class for the three terminals. Handles login (up to three tries),
 * reading input, and showing messages. Subclasses supply the verify step
 * and their own main menu.
 *
 * Author: Clark Berry
 */
public abstract class Terminal {
    private static final int MAX_ATTEMPTS = 3;

    protected String userNumber;
    protected boolean verified;
    private final Scanner scanner;

    protected Terminal(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Asks for the user's number, verifies it, and opens the main menu if it is valid. */
    public void start(String numberPrompt) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String number = readInput(numberPrompt);
            if (verify(number)) {
                userNumber = number;
                verified = true;
                display("Login successful. Welcome, " + welcomeName(number) + ".");
                showMainMenu();
                verified = false;
                return;
            }
            display("Invalid number. Attempts left: " + (MAX_ATTEMPTS - attempt));
        }
        display("Too many failed attempts. Returning to the start screen.");
    }

    /** Checks the number with VerifyController. */
    protected abstract boolean verify(String number);

    /** Returns the name to greet the user with. */
    protected abstract String welcomeName(String number);

    /** Shows the menu for this terminal until the user logs out. */
    public abstract void showMainMenu();

    public void display(String message) {
        System.out.println(message);
    }

    public String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /** Reads a menu choice from min to max, asking again until it is valid. */
    protected int readChoice(String prompt, int min, int max) {
        while (true) {
            String line = readInput(prompt);
            if (line.matches("\\d+")) {
                int choice = Integer.parseInt(line);
                if (choice >= min && choice <= max) {
                    return choice;
                }
            }
            display("  Please enter a number from " + min + " to " + max + ".");
        }
    }

    /** Asks a yes or no question. */
    protected boolean confirm(String question) {
        while (true) {
            String line = readInput(question + " (y/n): ").toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            display("  Please enter y or n.");
        }
    }
}
