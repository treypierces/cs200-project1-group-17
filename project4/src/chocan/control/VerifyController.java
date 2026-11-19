package chocan.control;

import chocan.data.ProviderRepository;
import chocan.entity.Provider;
import chocan.entity.StaffAccount;

import java.util.List;

/**
 * Checks the number a user types at a terminal. Providers are looked up in the
 * provider file; operators and managers are checked against the staff accounts.
 *
 * Author: Clark Berry
 */
public class VerifyController {
    private final ProviderRepository providerRepo;
    private final List<StaffAccount> staffAccounts;

    public VerifyController(ProviderRepository providerRepo, List<StaffAccount> staffAccounts) {
        this.providerRepo = providerRepo;
        this.staffAccounts = staffAccounts;
    }

    public boolean verifyProvider(String providerNumber) {
        return providerNumber.matches("\\d{9}") && providerRepo.findProvider(providerNumber) != null;
    }

    public boolean verifyOperator(String operatorNumber) {
        return findStaff(operatorNumber, StaffAccount.OPERATOR) != null;
    }

    public boolean verifyManager(String managerNumber) {
        return findStaff(managerNumber, StaffAccount.MANAGER) != null;
    }

    /** Returns the name of the provider or staff member with this number, for the welcome message. */
    public String getUserName(String number) {
        Provider p = providerRepo.findProvider(number);
        if (p != null) {
            return p.getName();
        }
        for (StaffAccount s : staffAccounts) {
            if (s.matches(number, StaffAccount.OPERATOR) || s.matches(number, StaffAccount.MANAGER)) {
                return s.getName();
            }
        }
        return number;
    }

    private StaffAccount findStaff(String staffNumber, String role) {
        for (StaffAccount s : staffAccounts) {
            if (s.matches(staffNumber, role)) {
                return s;
            }
        }
        return null;
    }
}
