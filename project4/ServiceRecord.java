package chocan.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * One billed service. The received date and time is set when the record is created.
 * The fee is stored so past records keep the fee they were billed at.
 *
 * Author: Richard Norman
 */
public class ServiceRecord {
    private final LocalDateTime receivedDateTime;
    private final LocalDate serviceDate;
    private final String providerNumber;
    private final String memberNumber;
    private final String serviceCode;
    private final double fee;
    private final String comments;

    /** Creates a new record received now. */
    public ServiceRecord(LocalDate serviceDate, String providerNumber, String memberNumber,
                         String serviceCode, double fee, String comments) {
        this(LocalDateTime.now().withNano(0), serviceDate, providerNumber, memberNumber,
                serviceCode, fee, comments);
    }

    /** Recreates a saved record (used when loading from the data file). */
    public ServiceRecord(LocalDateTime receivedDateTime, LocalDate serviceDate, String providerNumber,
                         String memberNumber, String serviceCode, double fee, String comments) {
        this.receivedDateTime = receivedDateTime;
        this.serviceDate = serviceDate;
        this.providerNumber = providerNumber;
        this.memberNumber = memberNumber;
        this.serviceCode = serviceCode;
        this.fee = fee;
        this.comments = comments;
    }

    public LocalDateTime getReceivedDateTime() { return receivedDateTime; }
    public LocalDate getServiceDate() { return serviceDate; }
    public String getProviderNumber() { return providerNumber; }
    public String getMemberNumber() { return memberNumber; }
    public String getServiceCode() { return serviceCode; }
    public double getFee() { return fee; }
    public String getComments() { return comments; }
}
