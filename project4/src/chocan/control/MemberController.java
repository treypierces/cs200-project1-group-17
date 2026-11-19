package chocan.control;

import chocan.data.MemberRepository;
import chocan.entity.Address;
import chocan.entity.Member;
import chocan.entity.Person;

import java.util.List;

/**
 * Member rules: validating a member number at the provider terminal, and adding,
 * updating, and deleting member records for the operator.
 *
 * Author: Adam Davis
 */
public class MemberController {
    public static final String VALIDATED = "Validated";
    public static final String INVALID = "Invalid Number";
    public static final String SUSPENDED = "Member Suspended";

    private final MemberRepository memberRepo;

    public MemberController(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    /** Returns Validated, Invalid Number, or Member Suspended. */
    public String validateMember(String memberNumber) {
        String status = memberNumber.matches("\\d{9}") ? memberRepo.getMemberStatus(memberNumber) : null;
        if (status == null) {
            return INVALID;
        }
        return Member.ACTIVE.equals(status) ? VALIDATED : SUSPENDED;
    }

    /** Checks the data against Appendix A field sizes. Returns the problems, or "" if valid. */
    public String validateData(String name, Address address) {
        return Person.checkData(name, address);
    }

    /** Adds a new active member and returns the new member number, or null if the data is invalid. */
    public String addMember(String name, Address address) {
        if (!validateData(name, address).isEmpty()) {
            return null;
        }
        Member member = new Member(memberRepo.nextMemberNumber(), name, address);
        memberRepo.addMember(member);
        return member.getNumber();
    }

    public Member getMember(String memberNumber) {
        return memberRepo.findMember(memberNumber);
    }

    public boolean updateMember(String memberNumber, String name, Address address) {
        Member member = memberRepo.findMember(memberNumber);
        if (member == null || !validateData(name, address).isEmpty()) {
            return false;
        }
        member.update(name, address);
        return memberRepo.saveMember(member);
    }

    public boolean deleteMember(String memberNumber) {
        return memberRepo.removeMember(memberNumber);
    }

    /** Sets the member ACTIVE if fees are paid, otherwise SUSPENDED. */
    public boolean updateMemberStatus(String memberNumber, boolean feesPaid) {
        Member member = memberRepo.findMember(memberNumber);
        if (member == null) {
            return false;
        }
        member.setStatus(feesPaid ? Member.ACTIVE : Member.SUSPENDED);
        return memberRepo.saveMember(member);
    }

    public List<Member> listMembers() {
        return memberRepo.getAllMembers();
    }
}
