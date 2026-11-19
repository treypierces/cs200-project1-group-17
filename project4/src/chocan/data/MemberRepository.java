package chocan.data;

import chocan.entity.Address;
import chocan.entity.Member;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all members and saves them to data/members.txt after every change.
 * The first line of the file holds the next member number, so a deleted
 * member's number is never given to someone new.
 *
 * Author: Adam Davis
 */
public class MemberRepository {
    private final Path fileName;
    private final List<Member> members = new ArrayList<>();
    private long nextNumber = 100000001L;

    public MemberRepository(Path dataDir) {
        this.fileName = dataDir.resolve("members.txt");
        load();
    }

    /** Returns the member with this number, or null if there is none. */
    public Member findMember(String memberNumber) {
        for (Member m : members) {
            if (m.getNumber().equals(memberNumber)) {
                return m;
            }
        }
        return null;
    }

    /** Returns ACTIVE or SUSPENDED, or null if the member does not exist. */
    public String getMemberStatus(String memberNumber) {
        Member m = findMember(memberNumber);
        return m == null ? null : m.getStatus();
    }

    public String nextMemberNumber() {
        return String.format("%09d", nextNumber);
    }

    public boolean addMember(Member member) {
        members.add(member);
        nextNumber = Math.max(nextNumber, Long.parseLong(member.getNumber()) + 1);
        return save();
    }

    /** Saves changes made to a member that is already stored. */
    public boolean saveMember(Member member) {
        return findMember(member.getNumber()) != null && save();
    }

    public boolean removeMember(String memberNumber) {
        Member m = findMember(memberNumber);
        return m != null && members.remove(m) && save();
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    private void load() {
        if (!DataFile.exists(fileName)) {
            for (Member m : SeedData.members()) {
                members.add(m);
                nextNumber = Math.max(nextNumber, Long.parseLong(m.getNumber()) + 1);
            }
            save();
            return;
        }
        for (String[] f : DataFile.readRows(fileName)) {
            if (f[0].equals("NEXT")) {
                nextNumber = Long.parseLong(f[1]);
            } else {
                members.add(new Member(f[0], f[1], new Address(f[2], f[3], f[4], f[5]), f[6]));
            }
        }
    }

    private boolean save() {
        List<String> lines = new ArrayList<>();
        lines.add(DataFile.join("NEXT", String.valueOf(nextNumber)));
        for (Member m : members) {
            Address a = m.getAddress();
            lines.add(DataFile.join(m.getNumber(), m.getName(), a.getStreet(), a.getCity(),
                    a.getState(), a.getZip(), m.getStatus()));
        }
        return DataFile.writeRows(fileName, lines);
    }
}
