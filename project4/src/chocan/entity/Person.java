package chocan.entity;

/**
 * Name, nine digit number, and address shared by members and providers.
 *
 * Author: Clark Berry
 */
public abstract class Person {
    public static final int NAME_MAX = 25;

    protected String name;
    protected final String number;
    protected Address address;

    protected Person(String number, String name, Address address) {
        this.number = number;
        this.name = name;
        this.address = address;
    }

    public String getName() { return name; }
    public String getNumber() { return number; }
    public Address getAddress() { return address; }

    /** Replaces the name and address (used by Update Member and Update Provider). */
    public void update(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    /** Returns problems with a name and address, or an empty string if both are valid. */
    public static String checkData(String name, Address address) {
        String problems = Address.isText(name, NAME_MAX) ? "" : "Name must be 1 to 25 characters. ";
        return (problems + address.check()).trim();
    }
}
