package com.laf.mall.api.service;

import com.laf.mall.api.dto.Coupon;
import com.laf.mall.api.dto.RaffleInfo;
import com.laf.mall.api.querycondition.raffle.MyRewardQueryCondition;
import com.laf.mall.api.querycondition.raffle.RaffleRewardSaveCondition;
import com.laf.mall.api.vo.MyRewardInfo;
import com.laf.manager.core.exception.MallDBException;

import java.util.List;

public interface RaffleService {

    /**
     * 核实抽奖资格
     *
     * @param memberId
     * @param activityCode
     * @return -1: 没有抽奖
     * 0: 有资格
     */
    int verifyRaffle(final int memberId, final String activityCode);

    /**
     * 抽奖信息展示
     *
     * @return
     */
    RaffleInfo getRaffleInfo(final String activityCode, final Integer mallId);

    /**
     * 抽奖
     *
     * @param condition
     * @return 1：操作成功
     *         other：操作失败
     * @throws MallDBException
     */
    int toRaffle(final String activityCode, final RaffleRewardSaveCondition condition);

    /**
     * 获取我的奖品列表
     *
     * @param condition
     * @return
     */
    List<MyRewardInfo> getMyRewardList(final MyRewardQueryCondition condition);
}
