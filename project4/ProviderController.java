package chocan.control;

import chocan.data.ProviderRepository;
import chocan.entity.Address;
import chocan.entity.Person;
import chocan.entity.Provider;

import java.util.List;

/**
 * Adds, updates, and deletes provider records for the operator.
 *
 * Author: Samuel Harvey
 */
public class ProviderController {
    private final ProviderRepository providerRepo;

    public ProviderController(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    /** Checks the data against Appendix A field sizes. Returns the problems, or "" if valid. */
    public String validateData(String name, Address address) {
        return Person.checkData(name, address);
    }

    /** Adds a new provider and returns the new provider number, or null if the data is invalid. */
    public String addProvider(String name, Address address) {
        if (!validateData(name, address).isEmpty()) {
            return null;
        }
        Provider provider = new Provider(providerRepo.nextProviderNumber(), name, address);
        providerRepo.addProvider(provider);
        return provider.getNumber();
    }

    public Provider getProvider(String providerNumber) {
        return providerRepo.findProvider(providerNumber);
    }

    public boolean updateProvider(String providerNumber, String name, Address address) {
        Provider provider = providerRepo.findProvider(providerNumber);
        if (provider == null || !validateData(name, address).isEmpty()) {
            return false;
        }
        provider.update(name, address);
        return providerRepo.saveProvider(provider);
    }

    public boolean deleteProvider(String providerNumber) {
        return providerRepo.removeProvider(providerNumber);
    }

    public List<Provider> listProviders() {
        return providerRepo.getAllProviders();
    }
}
