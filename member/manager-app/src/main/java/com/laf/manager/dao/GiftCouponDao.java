package com.laf.manager.dao;

import com.laf.manager.dto.Coupon;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.dto.VerificationWide;
import com.laf.manager.querycondition.coupon.CouponLogFilterCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.manager.repository.GiftCouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GiftCouponDao {

    @Autowired
    GiftCouponRepository repository;

    public int saveGiftCoupon(final ReceiveCouponInfo coupon) {
        return repository.insertGiftCoupon(coupon);
    }

    public int editGiftCoupon(final ReceiveCouponInfo coupon) {
        return repository.updateGiftCoupon(coupon);
    }

    public List<ReceiveCouponInfo> getGiftCouponInfoList(GiftCouponQueryCondition condition) {
        return repository.selectGiftCouponInfoList(condition);
    }


    public int getGiftCouponsCount(final GiftCouponQueryCondition condition) {
        return repository.selectGiftCouponsCount(condition);
    }

    public ReceiveCouponInfo getGiftCouponInfoById(final Integer couponId) {
        return repository.selectGiftCouponInfoById(couponId);
    }

    public int saveVerificationWide(VerificationWide vw) {
        return repository.insertVerificationWide(vw);
    }

    public int deleteVerificationWideByCoupon(final Integer couponId) {
        return repository.deleteVerificationWideByCoupon(couponId);
    }

    public int multipleCouponLogsCount(final CouponLogFilterCondition condition) {
        return repository.multipleSelectCouponLogsCount(condition);
    }

    public BigDecimal getCouponLogsSum(CouponLogFilterCondition condition) {
        return repository.selectCouponLogsSum(condition);
    }

    public List<Coupon> multipleQuery(final CouponLogFilterCondition condition) {
        List<Coupon> $ = null;

        try {
            $ = repository.multipleSelectCouponLogs(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public List<ReceiveCouponInfo> getGiftCouponInfoListForRaffle(GiftCouponQueryCondition condition) {
        return repository.selectGiftCouponInfoListForRaffle(condition);
    }
}
