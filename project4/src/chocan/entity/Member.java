package chocan.entity;

/**
 * A ChocAn member. Status is ACTIVE or SUSPENDED (suspended when fees are not paid).
 *
 * Author: Adam Davis
 */
public class Member extends Person {
    public static final String ACTIVE = "ACTIVE";
    public static final String SUSPENDED = "SUSPENDED";

    private String status;

    public Member(String number, String name, Address address) {
        this(number, name, address, ACTIVE);
    }

    public Member(String number, String name, Address address, String status) {
        super(number, name, address);
        this.status = status;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isActive() { return ACTIVE.equals(status); }

    @Override
    public String toString() {
        return String.format("%s  %-25s  %-9s  %s", number, name, status, address);
    }
}
