package chocan.entity;

/**
 * A ChocAn staff member (operator or manager) who logs in with a staff number.
 *
 * Author: Clark Berry
 */
public class StaffAccount {
    public static final String OPERATOR = "OPERATOR";
    public static final String MANAGER = "MANAGER";

    private final String staffNumber;
    private final String role;
    private final String name;

    public StaffAccount(String staffNumber, String role, String name) {
        this.staffNumber = staffNumber;
        this.role = role;
        this.name = name;
    }

    public String getName() { return name; }

    /** True if this account has the given number and role. */
    public boolean matches(String staffNumber, String role) {
        return this.staffNumber.equals(staffNumber) && this.role.equals(role);
    }
}
