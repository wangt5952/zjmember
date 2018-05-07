package com.laf.manager.dao;

import com.laf.manager.dto.Member;
import com.laf.manager.dto.VerificationClerk;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.querycondition.member.MemberListQueryCondition;
import com.laf.manager.repository.MemberRepository;
import com.laf.manager.repository.VerificationClerkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemberDao {

    @Autowired
    MemberRepository repository;

    @Autowired
    VerificationClerkRepository verificationClerkRepository;

    public int saveMember(final Member member) {
        return repository.insertMemeber(member);
    }

    public Member getMemberById(final Integer memberId, final Integer mallId) {
        return repository.selectMemberById(memberId, mallId);
    }

    public Member getMemberByMobile(final String mobile) {
        return repository.selectMemberByMobile(mobile);
    }

    public Member getMemberById(final Integer memberId) {
        return repository.selectMemberById(memberId);
    }

    public Member getSimpleMemberByMobile(final String mobile) {
        return repository.selectSimpleMemberByMobile(mobile);
    }

    public Member getMemberByOpenIdInMall(final String appId, final String openId) {
        return repository.selectMemberByOpenIdInMall(appId, openId);
    }

    public int updateMember(final Member member) {
        return repository.updateMember(member);
    }

    public int updateMobile(final String mobile, final Integer memberId) {
        return repository.updateMobile(mobile, memberId);
    }

    public int updatePoints(int memberId, int cumulatePoints, int useablePoints) {
        return repository.updatePoints(memberId, cumulatePoints, useablePoints);
    }

    public int updateCumulateAmount(int memberId, BigDecimal amount) {
        return repository.updateCumulateAmount(memberId, amount);
    }

    public int updateLevel(int memberId, int levelId, String levelName) {
        return repository.updateLevel(memberId, levelId, levelName);
    }

    public int getMobileCount(final String mobile) {
        return repository.selectMobileCount(mobile);
    }

    public int saveMemberInMall(final Integer memberId, final Integer mallId, final String openId) {
        return repository.insertMemberInMall(memberId, mallId, openId);
    }

    public int saveClerk(VerificationClerk verificationClerk) {
        return verificationClerkRepository.insertVerificationClerkForServices(verificationClerk);
    }

    public int saveServices(VerificationClerk verificationClerk) {
        return verificationClerkRepository.insertVerificationClerkForServices(verificationClerk);
    }

    public List<Member> getMemberList(MemberListQueryCondition condition) {
        return repository.selectMemberList(condition);
    }

    public int getMembersCount(final MemberListQueryCondition condition) {
        return repository.selectMembersCount(condition);
    }

    public List<Member> multipleQuery(final MemberFilterCondition condition) {
        List<Member> $ = null;

        try {
            $ = repository.multipleSelectMembers(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public int multipleMembersCount(final MemberFilterCondition condition) {
        return repository.multipleSelectMembersCount(condition);
    }

    public BigDecimal getCumulateAmountSum(final MemberFilterCondition condition) {
        return repository.selectCumulateAmountSum(condition);
    }

    public List<Member> getMultipleAllMembers(final MemberFilterCondition $$) {
        return repository.multipleSelectAllMembers($$);
    }

    public List<Integer> getVisitTimesMonthly(final int mallId) {
        return repository.selectVisitTimesMonthly(mallId);
    }

    public List<Integer> getVisitCountMonthly(final int mallId) {
        return repository.selectVisitCountMonthly(mallId);
    }

    public List<Integer> getActiveMembersCountMonthly(final int mallId) {
        return repository.selectActiveMembersCountMonthly(mallId);
    }

    public int getMemberCountBySex(int sex) {
        return repository.selectMemberCountBySex(sex);
    }

    public int getMemberCountByAge(int min, int max) {
        return repository.selectMemberCountByAge(min, max);
    }

    public List<Integer> getMemberCountByBirthday() {
        return repository.selectMemberCountByBirthday();
    }

    public List<Integer> getRegisterCountMonthly(int mallId) {
        return repository.selectRegisterCountMonthly(mallId);
    }

    public List<Integer> getRegisterCountMonthly(String year,int mallId) {
        return repository.selectRegisterCountMonthly(year, mallId);
    }

    public int getMemberCountByYear(String year, int mallId) {
        return repository.selectMemberCountByYear(year, mallId);
    }
}
