package chocan.entity;

/**
 * A service in the Provider Directory: six digit code, name, and fee.
 *
 * Author: Samuel Harvey
 */
public class Service {
    private final String code;
    private final String name;
    private final double fee;

    public Service(String code, String name, double fee) {
        this.code = code;
        this.name = name;
        this.fee = fee;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getFee() { return fee; }
}
