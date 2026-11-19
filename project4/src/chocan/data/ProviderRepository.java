package chocan.data;

import chocan.entity.Address;
import chocan.entity.Provider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all providers and saves them to data/providers.txt after every change.
 * The first line of the file holds the next provider number.
 *
 * Author: Samuel Harvey
 */
public class ProviderRepository {
    private final Path fileName;
    private final List<Provider> providers = new ArrayList<>();
    private long nextNumber = 500000001L;

    public ProviderRepository(Path dataDir) {
        this.fileName = dataDir.resolve("providers.txt");
        load();
    }

    /** Returns the provider with this number, or null if there is none. */
    public Provider findProvider(String providerNumber) {
        for (Provider p : providers) {
            if (p.getNumber().equals(providerNumber)) {
                return p;
            }
        }
        return null;
    }

    public String nextProviderNumber() {
        return String.format("%09d", nextNumber);
    }

    public boolean addProvider(Provider provider) {
        providers.add(provider);
        nextNumber = Math.max(nextNumber, Long.parseLong(provider.getNumber()) + 1);
        return save();
    }

    /** Saves changes made to a provider that is already stored. */
    public boolean saveProvider(Provider provider) {
        return findProvider(provider.getNumber()) != null && save();
    }

    public boolean removeProvider(String providerNumber) {
        Provider p = findProvider(providerNumber);
        return p != null && providers.remove(p) && save();
    }

    public List<Provider> getAllProviders() {
        return new ArrayList<>(providers);
    }

    private void load() {
        if (!DataFile.exists(fileName)) {
            for (Provider p : SeedData.providers()) {
                providers.add(p);
                nextNumber = Math.max(nextNumber, Long.parseLong(p.getNumber()) + 1);
            }
            save();
            return;
        }
        for (String[] f : DataFile.readRows(fileName)) {
            if (f[0].equals("NEXT")) {
                nextNumber = Long.parseLong(f[1]);
            } else {
                providers.add(new Provider(f[0], f[1], new Address(f[2], f[3], f[4], f[5])));
            }
        }
    }

    private boolean save() {
        List<String> lines = new ArrayList<>();
        lines.add(DataFile.join("NEXT", String.valueOf(nextNumber)));
        for (Provider p : providers) {
            Address a = p.getAddress();
            lines.add(DataFile.join(p.getNumber(), p.getName(), a.getStreet(), a.getCity(),
                    a.getState(), a.getZip()));
        }
        return DataFile.writeRows(fileName, lines);
    }
}
