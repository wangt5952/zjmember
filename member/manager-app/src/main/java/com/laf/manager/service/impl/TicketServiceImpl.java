package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.*;
import com.laf.manager.dto.*;
import com.laf.manager.querycondition.points.PromotionQueryCondition;
import com.laf.manager.querycondition.ticket.*;
import com.laf.manager.service.SettingsService;
import com.laf.manager.service.TicketService;
import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketDao ticketDao;

    @Autowired
    PromotionPointsRuleDao promotionPointsRuleDao;

    @Autowired
    SimplePointsRuleDao simplePointsRuleDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    PointsDao pointsDao;

    @Autowired
    LevelDao levelDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    SettingsService settingsService;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Override
    public List<Ticket> getTicketList(TicketFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return ticketDao.multipleQuery(condition);
    }

    @Override
    @Transactional
    public int operateTicket(TicketSaveCondition condition) throws MallDBException {
        Member member = memberDao.getMemberById(condition.getMemberId());

        if (member == null) {
            return 0;
        }

        int mallId = settingsProperties.getMallId();
        int result = 0;
//        log.info("{}", condition.getAmount() + " " + condition.getShoppingDate() " " + condition.get);
        Ticket ticket = new Ticket();
        ticket.setTicket_id(condition.getTicketId());
        ticket.setVc_id(1); // TODO: 2017/11/12 获取操作人员的信息
        ticket.setVc_name("");
        ticket.setVc_phone("");
        ticket.setHandle_date(new Date());
        ticket.setAmounts(condition.getAmount());
        ticket.setHandle_status(0);
        ticket.setResponses(condition.getResponses());
        ticket.setTicket_no(condition.getTicketNo());
        if (condition.getShoppingDate() != null) ticket.setShopping_date(new Date(condition.getShoppingDate()));
        if (condition.getShopId() != null) ticket.setShop_id(condition.getShopId());
        ticket.setShop_name(condition.getShopName());
        result = ticketDao.updateTicket(ticket);

        if (result <= 0) return result;

        //在小票成功处理后，更新会员的累计消费,重新计算会员等级
        BigDecimal newCumulateAmount = condition.getAmount().add(condition.getCumulateAmount());
        result = memberDao.updateCumulateAmount(condition.getMemberId(), newCumulateAmount);

        if (result <= 0) throw new MallDBException();

        int upgrade = 0; // 升级奖励

        int newCumulatePoints = member.getCumulate_points();
        int newUseablePoints = member.getUsable_points();

        List<Level> list = levelDao.getLevelWidthAmount(newCumulateAmount);
        Level level = list.get(0);
        if (level != null && level.getLevel_id() != condition.getLevelId()) {
            result = memberDao.updateLevel(condition.getMemberId(), level.getLevel_id(), level.getLevel_name());
            upgrade = settingsService.getPointsByLevel(level.getLevel_id());
        }

        if (result <= 0) throw new MallDBException();

        //更新会员积分，如果有的话
        int pointsValue = condition.getPoints();

        if (pointsValue > 0) {
            // 乘以系数 四舍五入 取整
            float coefficient = settingsService.getPointsCoefficient();
            float coefficientPoints = pointsValue * coefficient;
            pointsValue = Math.round(coefficientPoints);

            newCumulatePoints += pointsValue;
            newUseablePoints += pointsValue;
            result = memberDao.updatePoints(condition.getMemberId(), newCumulatePoints, newUseablePoints);

            if (result <= 0) throw new MallDBException();

            Points points = new Points();
            points.setMember_id(member.getMember_id());
            points.setMember_mobile(member.getMobile());
            points.setMember_name(member.getName());
            points.setVc_id(1); // TODO: 2017/11/12 获取操作人员的信息
            points.setVc_name("");
            points.setHandle_date(new Date());
            points.setAmount(new BigDecimal(-condition.getAmount().longValue()));
            points.setPoints(pointsValue);
            points.setMall_id(mallId);
            points.setShop_id(condition.getShopId());
            points.setShop_name(condition.getShopName());
            points.setShopping_date(new Date(condition.getShoppingDate()));
            points.setTicket_no(condition.getTicketNo());
            points.setSources(0);
            result = pointsDao.savePoints(points);

            if (result <= 0) throw new MallDBException();
        }

        if (upgrade > 0) {
            newCumulatePoints += upgrade;
            newUseablePoints += upgrade;
            result = memberDao.updatePoints(condition.getMemberId(), newCumulatePoints, newUseablePoints);

            if (result <= 0) throw new MallDBException();

            Points points = new Points();
            points.setMember_id(member.getMember_id());
            points.setMember_mobile(member.getMobile());
            points.setMember_name(member.getName());
            points.setHandle_date(new Date());
            points.setAmount(new BigDecimal(0d));
            points.setPoints(upgrade);
            points.setMall_id(mallId);
            points.setSources(9);
            result = pointsDao.savePointsExcludeConsume(points);

            if (result <= 0) throw new MallDBException();
        }

        return result;
    }
//积分录入 小票列表，积分明细
    @Override
    @Transactional
    public int operateHandlePoints(TicketSaveCondition condition) throws MallDBException {
        Member member = memberDao.getMemberById(condition.getMemberId());

        if (member == null) {
            return 0;
        }

        int mallId = settingsProperties.getMallId();
        int result = 0;
//        log.info("{}", condition.getAmount() + " " + condition.getShoppingDate() " " + condition.get);
        Ticket ticket = new Ticket();
        ticket.setMember_id(member.getMember_id());
        ticket.setMember_name(member.getName());
        ticket.setMember_mobile(member.getMobile());
        ticket.setVc_id(1);
        ticket.setVc_name("");
        ticket.setVc_phone("");
        ticket.setHandle_date(new Date());
        ticket.setAmounts(condition.getAmount());
        ticket.setHandle_status(3);
        ticket.setResponses(condition.getResponses());
        ticket.setTicket_no(condition.getTicketNo());
        if (condition.getShoppingDate() != null) ticket.setShopping_date(new Date(condition.getShoppingDate()));
        if (condition.getShopId() != null) ticket.setShop_id(condition.getShopId());
        ticket.setShop_name(condition.getShopName());
        ticket.setMall_id(mallId);
        result = ticketDao.saveMultipleTicket(ticket);

        if (result <= 0) return result;

        //在小票成功处理后，更新会员的累计消费,重新计算会员等级
        BigDecimal newCumulateAmount = condition.getAmount().add(condition.getCumulateAmount());
        result = memberDao.updateCumulateAmount(condition.getMemberId(), newCumulateAmount);

        if (result <= 0) throw new MallDBException();

        int upgrade = 0; // 升级奖励

        int newCumulatePoints = member.getCumulate_points();
        int newUseablePoints = member.getUsable_points();

        List<Level> list = levelDao.getLevelWidthAmount(newCumulateAmount);
        Level level = list.get(0);

        if (level != null && level.getLevel_id() != condition.getLevelId()) {
            result = memberDao.updateLevel(condition.getMemberId(), level.getLevel_id(), level.getLevel_name());
            upgrade = settingsService.getPointsByLevel(level.getLevel_id());
        }

        if (result <= 0) throw new MallDBException();

        //更新会员积分，如果有的话
        int pointsValue = condition.getPoints();

        if (pointsValue > 0) {
            // 乘以系数 四舍五入 取整
            float coefficient = settingsService.getPointsCoefficient();
            float coefficientPoints = pointsValue * coefficient;
            pointsValue = Math.round(coefficientPoints);

            newCumulatePoints += pointsValue;
            newUseablePoints += pointsValue;
            result = memberDao.updatePoints(condition.getMemberId(), newCumulatePoints, newUseablePoints);

            if (result <= 0) throw new MallDBException();

            Points points = new Points();
            points.setMember_id(member.getMember_id());
            points.setMember_mobile(member.getMobile());
            points.setMember_name(member.getName());
            points.setVc_id(1); // TODO: 2017/11/12 获取操作人员的信息
            points.setVc_name("");
            points.setHandle_date(new Date());
            points.setAmount(new BigDecimal(-condition.getAmount().longValue()));
            points.setPoints(pointsValue);
            points.setMall_id(mallId);
            points.setShop_id(condition.getShopId());
            points.setShop_name(condition.getShopName());
            points.setShopping_date(new Date(condition.getShoppingDate()));
            points.setTicket_no(condition.getTicketNo());
            points.setSources(10);
            result = pointsDao.savePoints(points);

            if (result <= 0) throw new MallDBException();
        }

        if (upgrade > 0) {
            newCumulatePoints += upgrade;
            newUseablePoints += upgrade;
            result = memberDao.updatePoints(condition.getMemberId(), newCumulatePoints, newUseablePoints);

            if (result <= 0) throw new MallDBException();

            Points points = new Points();
            points.setMember_id(member.getMember_id());
            points.setMember_mobile(member.getMobile());
            points.setMember_name(member.getName());
            points.setHandle_date(new Date());
            points.setAmount(new BigDecimal(0d));
            points.setPoints(upgrade);
            points.setMall_id(mallId);
            points.setSources(9);
            result = pointsDao.savePointsExcludeConsume(points);

            if (result <= 0) throw new MallDBException();
        }

        return result;
    }

    @Override
    public int operateTicketForNoPass(TicketNoPassSaveCondition condition) {
        condition.setVcId(1); // TODO: 2017/11/15 获取操作人员的信息
        condition.setHandleDate(new Date().getTime());
        condition.setHandleStatus(1);
        int result = ticketDao.updateTicketForNoPass(condition);

        return result;
    }

    @Override
    public Ticket getTicketDetail(Integer ticketId) {
        return ticketDao.getTicketById(ticketId);
    }

    @Override
    public int getTicketsCount() {
        return ticketDao.getTicketsCount(settingsProperties.getMallId());
    }

    @Override
    public int getPoints(PointsGotCondition condition) {
        BigDecimal amount;

        PromotionQueryCondition promotionQueryCondition = new PromotionQueryCondition();
        promotionQueryCondition.setBirthday(condition.getBirthday());
        promotionQueryCondition.setLevelId(condition.getLevelId());
        promotionQueryCondition.setShopId(condition.getShopId());
        promotionQueryCondition.setShoppingDate(condition.getShoppingDate());
        PromotionPointsRule promotionPointsRule = promotionPointsRuleDao.getRule(promotionQueryCondition);

        if (promotionPointsRule == null) {
            amount = simplePointsRuleDao.getSimpleAmount(condition.getLevelId(), condition.getShopId());

        } else {
            amount = promotionPointsRule.getAmount();
        }
        int points = (int) (condition.getAmount().longValue() / amount.longValue());

        return points;
    }

    @Override
    public int removeTicketById(Integer ticketId) {
        int result = ticketDao.removeTicketById(ticketId);
        return result;
    }

    @Override
    public int getMultipleTicketsCount(TicketFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return ticketDao.multipleTicketsCount(condition);
    }

    @Override
    public int getTicketByTicketNoCount(final String ticketNo) {
        return ticketDao.getTicketByTicketNoCount(ticketNo);
    }
}
