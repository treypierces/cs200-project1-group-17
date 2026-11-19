package chocan.data;

import chocan.entity.Address;
import chocan.entity.Member;
import chocan.entity.Provider;
import chocan.entity.Service;
import chocan.entity.ServiceRecord;
import chocan.entity.StaffAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sample data used the first time the program runs (or after --reset), so every
 * feature can be shown right away. Service records are dated relative to today
 * so they fall inside the current week.
 *
 * Author: Clark Berry
 */
public final class SeedData {

    private SeedData() { }

    public static List<StaffAccount> staffAccounts() {
        return Arrays.asList(
                new StaffAccount("200000001", StaffAccount.OPERATOR, "Olivia Operator"),
                new StaffAccount("300000001", StaffAccount.MANAGER, "Morgan Manager"));
    }

    public static List<Member> members() {
        return Arrays.asList(
                new Member("100000001", "Alice Johnson", new Address("12 Oak St", "Tuscaloosa", "AL", "35401")),
                new Member("100000002", "Brian Smith", new Address("450 Pine Ave", "Northport", "AL", "35473")),
                new Member("100000003", "Carla Gomez", new Address("78 Elm Rd", "Tuscaloosa", "AL", "35404"), Member.SUSPENDED),
                new Member("100000004", "David Lee", new Address("9 Maple Dr", "Birmingham", "AL", "35203")),
                new Member("100000005", "Emma Brown", new Address("301 Cedar Ln", "Cottondale", "AL", "35453")));
    }

    public static List<Provider> providers() {
        return Arrays.asList(
                new Provider("500000001", "Dr. Sarah Patel", new Address("100 Health Way", "Tuscaloosa", "AL", "35401")),
                new Provider("500000002", "Mark Rivera", new Address("22 Wellness Blvd", "Northport", "AL", "35476")),
                new Provider("500000003", "Linda Chen", new Address("5 Fitness Ct", "Tuscaloosa", "AL", "35406")));
    }

    public static List<Service> services() {
        return Arrays.asList(
                new Service("598470", "Dietitian", 75.00),
                new Service("883948", "Aerobics Exercise", 40.00),
                new Service("112233", "Group Therapy", 50.00),
                new Service("445566", "Counseling Session", 85.00),
                new Service("778899", "Nutrition Class", 35.00),
                new Service("334455", "Health Assessment", 120.00));
    }

    public static List<ServiceRecord> records() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ServiceRecord> list = new ArrayList<>();
        list.add(record(now, 5, "500000001", "100000001", "598470", 75.00, "Initial diet plan"));
        list.add(record(now, 4, "500000001", "100000002", "445566", 85.00, ""));
        list.add(record(now, 3, "500000002", "100000001", "883948", 40.00, ""));
        list.add(record(now, 3, "500000002", "100000004", "883948", 40.00, "First session"));
        list.add(record(now, 2, "500000003", "100000005", "334455", 120.00, ""));
        list.add(record(now, 2, "500000001", "100000004", "598470", 75.00, ""));
        list.add(record(now, 1, "500000003", "100000002", "112233", 50.00, ""));
        // Older than one week, so it is left out of this week's reports.
        list.add(record(now, 10, "500000002", "100000005", "778899", 35.00, "Last week"));
        return list;
    }

    private static ServiceRecord record(LocalDateTime now, int daysAgo, String provider, String member,
                                        String code, double fee, String comments) {
        LocalDateTime received = now.minusDays(daysAgo);
        return new ServiceRecord(received, received.toLocalDate(), provider, member, code, fee, comments);
    }
}
