package com.laf.mall.api.service;

import com.laf.mall.api.dto.Points;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.points.PointsSaveCondition;
import com.laf.mall.api.querycondition.points.PromotionQueryCondition;
import com.laf.manager.core.support.tuple.Tuple2;

import java.math.BigDecimal;
import java.util.List;

public interface PointsService {

    /**
     * 获取会员的积分收入和支出的记录
     * @param condition
     * @return
     */
    List<Points> getPointsList(PointsQueryCondition condition, final Integer memberId);

    /**
     * 获取积分详情
     * @param mplogId
     * @return
     */
    Points getPointsDetail(final String mplogId);

    /**
     * 保存积分的收入和支出记录
     * @param points
     * @return
     */
    int savePoints(Points points);

    /**
     * 计算所有商铺积分
     * @param mallId
     * @return
     */
    int calculatePoints(final Integer mallId);

    /**
     * 计算促销积分
     * @param ruleId
     * @return
     */
    int calculatePromotionPoints(final Integer ruleId);

    /**
     * 获得普通积分兑换金额
     * @param levelId 会员等级
     * @param shopId 消费商铺
     * @return 消费额/1积分
     */
    BigDecimal getSimpleAmount(final Integer levelId, final Integer shopId);

    /**
     * 获得促销积分兑换金额
     * @param condition 查询条件
     * @return 消费额/1积分
     */
    BigDecimal getPromotionAmount(final PromotionQueryCondition condition);

    /**
     * 根据完善资料的赠送类型获取相应的积分，券
     * @param mallId
     * @return $1 积分；$2 券
     */
    Tuple2<Integer, Integer> getActionPointsByType2(final Integer mallId);

    /**
     * 删除普通积分规则
     * @param ruleId
     * @return
     */
    int deleteSimpleRule(final Integer ruleId);

    /**
     * 删除促销积分规则
     * @param ruleId
     * @return
     */
    int deletePromotionRule(final Integer ruleId);

    /**
     * 添加动作行为积分
     * @param condition
     * @return
     */
    int addPointsExcludeConsume(PointsActionCondition condition);

    /**
     * 获取积分抵现规则
     * @param type 类型
     * @param mallId
     * @return
     */
    int getPointsPayCashRuleByType(final int type, final int mallId);
}
