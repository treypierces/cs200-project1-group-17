package chocan.entity;

/**
 * Street address, city, state, and ZIP code for a member or provider.
 * Field sizes follow Appendix A.
 *
 * Author: Clark Berry
 */
public class Address {
    public static final int STREET_MAX = 25;
    public static final int CITY_MAX = 14;

    private final String street;
    private final String city;
    private final String state;
    private final String zip;

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state.toUpperCase();
        this.zip = zip;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }

    /** Returns a list of problems with the fields, or an empty string if they are all valid. */
    public String check() {
        StringBuilder problems = new StringBuilder();
        if (!isText(street, STREET_MAX)) problems.append("Street address must be 1 to 25 characters. ");
        if (!isText(city, CITY_MAX)) problems.append("City must be 1 to 14 characters. ");
        if (!state.matches("[A-Z]{2}")) problems.append("State must be 2 letters. ");
        if (!zip.matches("\\d{5}")) problems.append("ZIP code must be 5 digits. ");
        return problems.toString().trim();
    }

    /** True if s is 1 to max characters and has no '|' (the separator used in data files). */
    static boolean isText(String s, int max) {
        return s != null && !s.isEmpty() && s.length() <= max && !s.contains("|");
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zip;
    }
}
