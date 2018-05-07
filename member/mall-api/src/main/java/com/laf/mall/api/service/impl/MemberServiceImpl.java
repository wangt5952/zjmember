package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.*;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.menu.PointsSources;
import com.laf.mall.api.querycondition.assessment.AssessmentListCondition;
import com.laf.mall.api.querycondition.assessment.AssessmentSaveCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.member.*;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsPayCashCondition;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import com.laf.mall.api.querycondition.ticket.TicketUploadCondition;
import com.laf.mall.api.service.*;
import com.laf.mall.api.utils.coupon.CouponProperties;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import com.laf.mall.api.utils.sms.SmsUtil;
import com.laf.manager.core.exception.MallDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.laf.mall.api.enums.AssessmentStatus.UNPROCESSED;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    LevelDao levelDao;

    @Autowired
    TicketDao ticketDao;

    @Autowired
    MallDao mallDao;

    @Autowired
    CouponDao couponDao;

    @Autowired
    AssessmentDao assessmentDao;

    @Autowired
    PointsService pointsService;

    @Autowired
    CouponService couponService;

    @Autowired
    ShopService shopService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    SmsUtil smsUtil;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    CouponProperties couponProperties;

    @Override
    @Transactional
    public synchronized int registMember(final MemberRegistCondition condition) throws MallDBException {
        if (this.checkMobile(condition.getMobile()) == 0) return -1;

        Level level = levelDao.getLowestLevel(condition.getMallId());

        if (level == null) return 0;

        Member member = new Member();
        member.setOpen_id(condition.getOpenId());
        member.setName(condition.getName());
        member.setBirthday(new Date(condition.getBirthday()));
        member.setSex(condition.getSex());
        member.setMobile(condition.getMobile());
        member.setMall_id(condition.getMallId());
        member.setLevel_id(level.getLevel_id());
        member.setLevel(level.getLevel_name());
        member.setUsable_points(100);
        member.setCumulate_points(100);

        int newMemberId = memberDao.saveMember(member);
        int result = 0;

        if (newMemberId > 0) {
            result = memberDao.saveMemberInMall(newMemberId, member.getMall_id(), member.getOpen_id());

            if (result <= 0) throw new MallDBException();
        } else {
            return result;
        }

//        long now = dateTimeUtils.getMilliByToDay();
//        long except = dateTimeUtils.getMilliByDate(2017, 12, 2); //2017 12.2赠送其它券
//        if (now == except) {
//            return result;
//        }
        if (couponProperties.isIsrelated()) {
            int couponId = couponProperties.getCouponid(); //int couponId = couponService.getCouponByRegister(condition.getMallId());
            ReceiveCouponInfo info = couponService.getCouponInfoById(couponId, newMemberId);

            if (info == null) return result;

            CouponReceiveCondition receiveCondition = new CouponReceiveCondition();
            receiveCondition.setCouponId(couponId);
            receiveCondition.setCouponStatus(1);
            receiveCondition.setMallId(info.getMall_id());
            receiveCondition.setMemberId(newMemberId);
            receiveCondition.setSources(0);
            result = couponService.saveCouponForMember(receiveCondition);
        }

        return result;
    }

    @Override
    public Member getMemberById(final MemberQueryCondition condition) {
        return memberDao.getMemberById(condition.getMemberId(), condition.getMallId());
    }

    @Override
    public Member getMemberByMobile(final String mobile) {
        return memberDao.getMemberByMobile(mobile);
    }

    @Override
    public Member getSimpleMemberById(final Integer memberId) {
        return memberDao.getMemberById(memberId);
    }

    @Override
    public Member getSimpleMemberByMobile(String mobile) {
        return memberDao.getMemberByMobile(mobile);
    }

    @Override
    public Member getBaseInfoByMobile(String mobile) {
        return memberDao.getSimpleMemberByMobile(mobile);
    }

    @Override
    public Member getMemberByOpenIdInMall(final String appId, final String openId) {
        return memberDao.getMemberByOpenIdInMall(appId, openId);
    }

    @Override
    @Transactional
    public int editMemberInfo(final MemberUpdateCondition condition) throws MallDBException {
        Member member = memberDao.getMemberById(condition.getMemberId());

        if (member == null) return 0;

        if (!StringUtils.isEmpty(condition.getName())) member.setName(condition.getName());

        if (condition.getSex() >= 0) member.setSex(condition.getSex());

        if (condition.getOccupation() >= 0) member.setOccupation(condition.getOccupation());

        if (!StringUtils.isEmpty(condition.getAddress())) member.setAddress(condition.getAddress());

        if (condition.getDegreeOfEducation() >= 0) member.setDegree_of_education(condition.getDegreeOfEducation());

        if (condition.getIncomeRange() >= 0) member.setIncome_range(condition.getIncomeRange());

        if (!StringUtils.isEmpty(condition.getInterest())) member.setInterest(condition.getInterest());

        if (condition.getEnablePublicWa() != null) member.setEnable_public_wa(condition.getEnablePublicWa());

        if (condition.getWechatAccount() != null) member.setWechat_account(condition.getWechatAccount());

        if (!StringUtils.isEmpty(condition.getMobile())) member.setMobile(condition.getMobile());

        int result = 0;

        if (member.getEdit_flag() <= 0) {
            int points = mallDao.getActionPointsByType(2, member.getMall_id());
            member.setUsable_points(member.getUsable_points() + points);
            member.setCumulate_points(member.getCumulate_points() + points);

            PointsActionCondition actionCondition = new PointsActionCondition();
            actionCondition.setMallId(member.getMall_id());
            actionCondition.setMemberId(condition.getMemberId());
            actionCondition.setMemberMobile(member.getMobile());
            actionCondition.setMemberName(member.getName());
            actionCondition.setPoints(points);
            actionCondition.setSources(PointsSources.PERFECT_INFORMATION.$1);
            actionCondition.setHandleDate(new Date());

            result = pointsService.addPointsExcludeConsume(actionCondition);
        } else {
            result = 1;
        }

        if (result > 0) {
            boolean modifiable = member.getEdit_flag() <= 0;

            member.setEdit_flag(member.getEdit_flag() + 1);

            if (member.getBirthday_modified() > 0 ||
                    (condition.getBirthday() == 0 || member.getBirthday() == condition.getBirthday())) {

                member.setBirthday(new Date(member.getBirthday()));
                member.setBirthday_modified(member.getBirthday_modified());

            } else {

                member.setBirthday(new Date(condition.getBirthday()));
                member.setBirthday_modified(member.getBirthday_modified() + 1);
            }

            result = memberDao.updateMember(member);

            if (result <= 0 && modifiable) {
                throw new MallDBException();
            }
        }

        return result;
    }

    @Override
    public int sendSmsVerificationCode(String mobile, Integer mallId) throws UnsupportedEncodingException {
        if (this.checkMobile(mobile) == 0) return -1;

        String validCode = smsUtil.getValidCode();

        ResponseEntity<String> responseEntity = smsUtil.sendSmsByTemplate(mobile, validCode);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            redisTemplate.opsForValue().set(mobile, validCode);
            return 1;
        }

        return 0;
    }

    @Override
    public int checkValidCode(String mobile, String validCode) {
        String code = redisTemplate.opsForValue().get(mobile);

        if (!StringUtils.isEmpty(code) && code.equals(validCode)) {
            redisTemplate.delete(mobile);
            return 1;
        }

        return 0;
    }

    @Override
    public int checkMobile(String mobile) {
        return memberDao.getMobileCount(mobile) == 1 ? 0 : 1;
    }

    @Override
    public int updateMobileById(String mobile, Integer memberId) {
        return memberDao.updateMobile(mobile, memberId);
    }

    @Override
    @Transactional
    public int uploadTicket(String fileUrl, Integer memberId, TicketUploadCondition condition) throws MallDBException {
        Member member = memberDao.getMemberById(memberId);
        if (member == null) return 0;

        Ticket ticket = new Ticket();
        ticket.setMember_id(memberId);
        ticket.setMember_name(member.getName());
        ticket.setMember_mobile(member.getMobile());
        ticket.setFile_url(fileUrl);
        ticket.setUpload_date(new Date());
        ticket.setMall_id(condition.getMallId());
        ticket.setAmounts(condition.getAmount());
        ticket.setShop_id(condition.getShopId());
        ticket.setShopping_date(new Date(condition.getShoppingDate()));
        if (condition.getPoints() >= 0) ticket.setTicket_no(String.valueOf(condition.getPoints()));
        int result = ticketDao.saveTicket(ticket);

        if (result <= 0) throw new MallDBException(0);

        return result;
    }

    @Override
    public int removeTicket(Integer ticketId) {
        return ticketDao.removeTicketById(ticketId);
    }

    @Override
    public List<Ticket> getTicketListByMe(TicketQueryCondition condition) {
        return ticketDao.getTicketListByMemberId(condition);
    }

    @Override
    public int registServices(ServicesRegistCondition condition) {
        VerificationClerk services = new VerificationClerk();
        services.setVc_name(condition.getName());
        services.setPhone(condition.getPhone());
        services.setDepartment(condition.getDepartment());
        services.setReg_date(new Date());
        services.setVc_type(0);
        services.setMember_id(condition.getMemberId());
        services.setMall_id(condition.getMallId());

        return memberDao.saveServices(services);
    }

    @Override
    public int registClerk(ClerkRegistCondition condition) {
        VerificationClerk clerk = new VerificationClerk();
        clerk.setVc_name(condition.getName());
        clerk.setPhone(condition.getPhone());
        clerk.setShop_id(condition.getShopId());
        clerk.setReg_date(new Date());
        clerk.setVc_type(1);
        clerk.set_manager(false);
        clerk.setMember_id(condition.getMemberId());
        clerk.setMall_id(condition.getMallId());
        return memberDao.saveClerk(clerk);
    }

    @Override
    public int getUsablePoints4Member(final Integer memberId) {
        return memberDao.getUsablePoints4Member(memberId);
    }

    /**
     * @param memberId
     * @param couponId
     * @return 0 已经终止发行
     * 1 库存不足
     * 2 当天已不能领取
     * 3 已领取券组中的其他券
     * 4 积分不足
     * 5 已领取
     * 6 条件满足可以领取
     * 7 领取失败(数据库操作失败，非逻辑错误)
     * 8 领取成功
     * @throws MallDBException
     */
    @Override
    @Transactional
    public int receiveCouponByMember(final int memberId, final int couponId) throws MallDBException {

        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            return 7;
        }

        ReceiveCouponInfo info = couponService.getCouponInfoById(couponId, memberId);

        if (info.getLimitPromptCode() != 6) return info.getLimitPromptCode(); //领取之前判断能否领取

        CouponReceiveCondition condition = new CouponReceiveCondition();
        if (info.getCoupon_type() == 0) {
            condition.setCouponStatus(1);
        } else if (info.getCoupon_type() == 1) {
            condition.setCouponStatus(0);
        }
        condition.setCouponId(couponId);
        condition.setMemberId(memberId);
        condition.setMallId(info.getMall_id());
        int result = couponService.saveCouponForMember(condition);

        if (result <= 0) return 7;

        if (info.getReceive_method() == 0) {
            PointsActionCondition actionCondition = new PointsActionCondition();
            actionCondition.setMemberId(member.getMember_id());
            actionCondition.setMemberName(member.getName());
            actionCondition.setMemberMobile(member.getMobile());
            actionCondition.setMallId(member.getMall_id());
            actionCondition.setSources(6);
            actionCondition.setPoints(info.getRequired_points() * -1);
            actionCondition.setHandleDate(new Date(condition.getCurrTime())); //检查处理时间是否为年月日
            result = pointsService.addPointsExcludeConsume(actionCondition);

            if (result <= 0) throw new MallDBException(7);

            member.setUsable_points(member.getUsable_points() - info.getRequired_points());
            result = memberDao.updateMember(member);

            if (result <= 0) throw new MallDBException(7);
        }

        return 8;
    }

    @Override
    public VerificationClerk getVerificationClerkByMember(final Integer memberId) {
        return memberDao.getVerificationClerk(memberId);
    }

    /**
     * 提交评价建议
     * 0：会员不存在
     * 1：提交失败
     * 2：提交成功
     *
     * @param condition
     * @return
     * @throws MallDBException
     */
    @Override
    @Transactional
    public int submitAssessment(final AssessmentSaveCondition condition, final Integer memberId) throws MallDBException {
        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            return 0;
        }
        Assessment assessment = new Assessment();
        if (condition.getShopId() == null || condition.getShopId() <= 0) {
//            assessment.setShop_id(103);
            assessment.setShop_id(111);
            assessment.setShop_name("客服中心");

        } else {
            Shop shop = shopService.getShopDetail(condition.getShopId());
            if (shop == null) return -1;
            assessment.setShop_id(condition.getShopId());
            assessment.setShop_name(shop.getShop_name());
        }
        assessment.setMall_id(condition.getMallId());
        assessment.setMember_id(member.getMember_id());
        assessment.setMember_name(member.getName());
        assessment.setMember_mobile(member.getMobile());
        assessment.setAction_time(condition.getActionTime());
        assessment.setStatus(UNPROCESSED.ordinal());
        assessment.setComments(condition.getComments());
        int newId = assessmentDao.saveAssessment(assessment);

        if (newId <= 0) throw new MallDBException(1);

        // TODO: 2018/1/3 获取积分
        return 2;
    }

    @Override
    public List<Assessment> getAssessmentList(final AssessmentListCondition condition, final Integer memberId) {
        return assessmentDao.getAssessmentList(condition, memberId);
    }

    @Override
    public Assessment getAssessmentInfo(final Integer assessmentId) {
        return assessmentDao.getAssessmentById(assessmentId);
    }

    /**
     * @param condition
     * @param memberId
     * @return 0 会员不存在
     * 1 商户不存在
     * 2 抵现规则不存在
     * 3 积分不足
     * 4 抵现失败
     * 5 抵现成功
     */
    @Override
    @Transactional
    public int pointsPayCash(final PointsPayCashCondition condition, final Integer memberId) throws MallDBException {
        //shopId,memberId,points
        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            return 0;
        }

        Shop shop = shopService.getShopDetail(condition.getShopId());
        if (shop == null) return 1;
        //获取抵现比例，计算积分兑换金额，录入积分日志，更新会员积分
        int rule = pointsService.getPointsPayCashRuleByType(0, condition.getMallId());
        if (rule <= 0) return 2;

        if (condition.getPoints() < rule) return 3;

        int amount = Math.round(condition.getPoints() / rule);
        Points points = new Points();
        points.setAmount(new BigDecimal(amount));
        points.setPoints(-condition.getPoints());
        points.setMember_id(member.getMember_id());
        points.setMember_name(member.getName());
        points.setMember_mobile(member.getMobile());
        points.setShop_name(shop.getShop_name());
        points.setShop_id(shop.getShop_id());
        points.setVc_id(1);
        points.setVc_name("");
        points.setHandle_date(condition.getCurrTime());
        points.setMall_id(condition.getMallId());
        int result = pointsService.savePoints(points);

        if (result <= 0) throw new MallDBException(4);

        member.setUsable_points(member.getUsable_points() - condition.getPoints());
        result = memberDao.updateMember(member);

        if (result <= 0) throw new MallDBException(4);

        return 5;
    }

    @Override
    public int loggingVisitor(MemberVisitSaveCondition condition) {
        Member member = memberDao.getMemberById(condition.getMemberId());
        if (member == null) return 0;

        MemberVisitLog log = new MemberVisitLog();

        log.setMall_id(condition.getMallId());
        log.setMember_id(condition.getMemberId());
        log.setOpen_id(condition.getOpenId());
        log.setVisit_time(new Date(condition.getCurr()));
        log.setSimple_time(new Date(condition.getSimpleTime()));

        int result = memberDao.saveMemberVisitLog(log);

        return result;
    }

    @Override
    public int updatePoints(int memberId, int cumulatePoints, int useablePoints) {
        return memberDao.updatePoints(memberId, cumulatePoints, useablePoints);
    }
}
