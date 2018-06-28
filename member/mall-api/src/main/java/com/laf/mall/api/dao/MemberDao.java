package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Member;
import com.laf.mall.api.dto.MemberVisitLog;
import com.laf.mall.api.dto.VerificationClerk;
import com.laf.mall.api.repository.MemberRepository;
import com.laf.mall.api.repository.VerificationClerkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    public int getMobileCount(final String mobile) {
        return repository.selectMobileCount(mobile);
    }

    public int saveMemberInMall(final Integer memberId, final Integer mallId, final String openId) {
        return repository.insertMemberInMall(memberId, mallId, openId);
    }

    public int saveClerk(VerificationClerk verificationClerk) {
        return verificationClerkRepository.insertVerificationClerkForClerk(verificationClerk);
    }

    public int saveServices(VerificationClerk verificationClerk) {
        return verificationClerkRepository.insertVerificationClerkForServices(verificationClerk);
    }

    public int getUsablePoints4Member(final Integer memberId) {
        return repository.selectUsablePoints4Member(memberId);
    }

    public long getRegistTime4Member(final Integer memberId, final Integer mallId) {
        return repository.selectRegistTime4Member(memberId, mallId);
    }

    public VerificationClerk getVerificationClerk(final int memberId) {
        return verificationClerkRepository.selectVerificationClerk(memberId);
    }

    public int updatePoints(int memberId, int cumulatePoints, int useablePoints) {
        return repository.updatePoints(memberId, cumulatePoints, useablePoints);
    }

    public int updateCumulateAmount(int memberId, BigDecimal amount) {
        return repository.updateCumulateAmount(memberId, amount);
    }

    public VerificationClerk getClerk(final int memberId, final int vcType, final int mallId) {
        return verificationClerkRepository.selectClerk(memberId, vcType, mallId);
    }

    public int saveMemberVisitLog(MemberVisitLog log) {
        return repository.insertMemberVisitLog(log);
    }

    public int updateLevel(int memberId, int levelId, String levelName) {
        return repository.updateLevel(memberId, levelId, levelName);
    }

}
