package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.MemberDao;
import com.laf.mall.api.dao.PointsDao;
import com.laf.mall.api.dao.RaffleDao;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.member.MemberQueryCondition;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.raffle.MyRewardQueryCondition;
import com.laf.mall.api.querycondition.raffle.RaffleRewardSaveCondition;
import com.laf.mall.api.service.CouponService;
import com.laf.mall.api.service.MemberService;
import com.laf.mall.api.service.PointsService;
import com.laf.mall.api.service.RaffleService;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.mall.api.vo.MyRewardInfo;
import com.laf.manager.core.exception.MallDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RaffleServiceImpl implements RaffleService {

    @Autowired
    RaffleDao raffleDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    MemberService memberService;

    @Autowired
    PointsDao pointsDao;

    @Autowired
    PointsService pointsService;

    @Autowired
    CouponService couponService;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Override
    public int verifyRaffle(int memberId, String activityCode) {
        int result = -1;

        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setMemberId(memberId);
        Member member = memberService.getMemberById(condition);
        if (member == null) return result;

        RaffleInfo raffleInfo = raffleDao.getRaffleInfoByActivityCode(activityCode, member.getMall_id());
        if (raffleInfo == null) return result;

        int requiredPoints = raffleInfo.getRequired_points();
        int usablePoints = memberService.getUsablePoints4Member(memberId);
        if (requiredPoints > usablePoints) return result;

//        int count = raffleInfo.getTimes() - raffleDao.getRaffleLogCount(memberId, raffleInfo.getRaffle_id());
//        if (count <= 0) return result;
//        int result = raffleDao.selectHasMemberWon(memberId, raffleInfo.getRaffle_id());
//        if (result > 0)        return 2;
//        else if (result == -1) return 3;
//        else                   return 4;

        result = 0;
        return result;
    }

    @Override
    public RaffleInfo getRaffleInfo(String activityCode, Integer mallId) {
        RaffleInfo raffleInfo = raffleDao.getRaffleInfoByActivityCode(activityCode, mallId);

        return raffleInfo;
    }

    @Transactional
    @Override
    public int toRaffle(String activityCode, RaffleRewardSaveCondition condition) throws MallDBException {
        RaffleInfo raffleInfo = raffleDao.getRaffleInfoByActivityCode(activityCode, condition.getMallId());
        RaffleReward reward = raffleDao.getRewardById(condition.getTrrId());
        MemberQueryCondition mCondition = new MemberQueryCondition();
        mCondition.setMemberId(condition.getMemberId());
        mCondition.setMallId(condition.getMallId());
        Member member = memberService.getMemberById(mCondition);
        int result = -1;

        if (raffleInfo == null || member == null) return result;

        RaffleLog log = new RaffleLog();
        log.setAction_time(new Date());
        log.setMember_id(member.getMember_id());
        log.setMember_name(member.getName());
        log.setMember_mobile(member.getMobile());
        log.setRaffle_id(raffleInfo.getRaffle_id());
        log.setTrr_id(reward == null ? 0 : reward.getTrr_id());
        result = raffleDao.saveRaffleLog(log);

        if (result <= 0) throw new MallDBException(result);

        PointsActionCondition actionCondition = new PointsActionCondition();
        actionCondition.setMemberId(member.getMember_id());
        actionCondition.setMemberName(member.getName());
        actionCondition.setMemberMobile(member.getMobile());
        actionCondition.setMallId(member.getMall_id());
        actionCondition.setSources(12);
        actionCondition.setPoints(raffleInfo.getRequired_points() * -1);
        actionCondition.setHandleDate(new Date());
        result = pointsService.addPointsExcludeConsume(actionCondition);

        if (result <= 0) throw new MallDBException(result);

        int newPoints = member.getUsable_points() - raffleInfo.getRequired_points();
        result = memberService.updatePoints(member.getMember_id(), member.getCumulate_points(), newPoints);

        if (result <= 0) throw new MallDBException(result);

        if (reward == null) return 1;

        switch (reward.getReward_type()) {
            case 0:
            case 1:
                CouponReceiveCondition cCondition = new CouponReceiveCondition();
                cCondition.setCouponId(reward.getReward_value());
                cCondition.setCouponStatus(1);
                cCondition.setMallId(condition.getMallId());
                cCondition.setMemberId(member.getMember_id());
                cCondition.setSources(3);
                cCondition.setSourceId(raffleInfo.getRaffle_id());
                result = couponService.saveCouponForMember(cCondition);

                if (result <= 0) throw new MallDBException(result);

//                if (reward.getInventory() > 0) {
//                    raffleDao.editRewardInventory(reward.getTrr_id(), reward.getInventory() - 1);
//                    if (result <= 0) throw new MallDBException(result);
//                }
                break;

            case 2:
                PointsActionCondition aCondition = new PointsActionCondition();
                aCondition.setMemberId(member.getMember_id());
                aCondition.setMemberName(member.getName());
                aCondition.setMemberMobile(member.getMobile());
                aCondition.setMallId(member.getMall_id());
                aCondition.setSources(13);
                aCondition.setPoints(reward.getReward_value());
                aCondition.setHandleDate(new Date());
                result = pointsService.addPointsExcludeConsume(aCondition);

                if (result <= 0) throw new MallDBException(result);

                result = memberService.updatePoints(member.getMember_id(),
                        member.getCumulate_points() + reward.getReward_value(),
                        member.getUsable_points() + reward.getReward_value());

                if (result <= 0) throw new MallDBException(result);

//                if (reward.getInventory() > 0) {
//                    raffleDao.editRewardInventory(reward.getTrr_id(), reward.getInventory() - 1);
//                    if (result <= 0) throw new MallDBException(result);
//                }

                break;
        }
        return 1;
    }

    @Override
    public List<MyRewardInfo> getMyRewardList(MyRewardQueryCondition condition) {
        List<MyRewardInfo> list = raffleDao.getMyRewardList(condition);

        return list;
    }
}
