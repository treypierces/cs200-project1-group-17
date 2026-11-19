package chocan.entity;

/**
 * A health care provider (dietitian, internist, exercise expert) who bills ChocAn.
 *
 * Author: Samuel Harvey
 */
public class Provider extends Person {

    public Provider(String number, String name, Address address) {
        super(number, name, address);
    }

    @Override
    public String toString() {
        return String.format("%s  %-25s  %s", number, name, address);
    }
}
