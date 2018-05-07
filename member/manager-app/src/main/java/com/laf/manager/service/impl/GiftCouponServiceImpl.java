package com.laf.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.ArticlesDao;
import com.laf.manager.dao.GiftCouponDao;
import com.laf.manager.dao.MemberDao;
import com.laf.manager.dao.ShopDao;
import com.laf.manager.dto.Coupon;
import com.laf.manager.dto.ReceiveCouponInfo;
import com.laf.manager.dto.Shop;
import com.laf.manager.dto.VerificationWide;
import com.laf.manager.enums.CouponStatus;
import com.laf.manager.querycondition.coupon.CouponLogFilterCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponEditCondition;
import com.laf.manager.querycondition.giftcoupon.GiftCouponQueryCondition;
import com.laf.manager.service.GiftCouponService;
import com.laf.manager.utils.datetime.DateTimeUtils;
import com.laf.manager.utils.poi.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class GiftCouponServiceImpl implements GiftCouponService {

    @Autowired
    GiftCouponDao couponDao;

    @Autowired
    ArticlesDao articlesDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    Generator generator;

    @Override
    public int editGiftCouponInfo(GiftCouponEditCondition condition) {
        ReceiveCouponInfo coupon = couponDao.getGiftCouponInfoById(condition.getCouponId());

        if (coupon == null) {
            coupon = new ReceiveCouponInfo();
        }

        coupon.setCoupon_name(condition.getCouponName());
        coupon.setReceive_method(condition.getReceiveMethod());
        coupon.setSort_id(condition.getSortId());
        coupon.setDaily_limited(condition.getDailyLimited());
        coupon.setLimited(condition.getLimited());
        coupon.setVerification_of(condition.getVerificationOf());
        coupon.setKeep_verification_of(condition.getKeepVerificationOf());
        coupon.setPrice(condition.getPrice());
        coupon.setCost_price(condition.getCostPrice());
        coupon.setDiscounted_price(condition.getDiscountedPrice());
        coupon.setRequired_points(condition.getRequiredPoints());
        coupon.setCirculation(condition.getCirculation());
        coupon.setDaily_circulation(condition.getDailyCirculation());
        coupon.setExpiry_date_start(new Date(condition.getExpiryDateStart()));
        coupon.setExpiry_date_end(new Date(condition.getExpiryDateEnd()));
        coupon.setIssue_start(new Date(condition.getIssueStart()));
        coupon.setIssue_end(new Date(condition.getIssueEnd()));
        coupon.setReg_time_start(new Date(condition.getRegTimeStart()));
        coupon.setReg_time_end(new Date(condition.getRegTimeEnd()));
        coupon.setMall_id(settingsProperties.getMallId());
        coupon.setStatus(1);
        coupon.setVerification_type(condition.getVerificationType());
        coupon.setShops(condition.getShops());

        int result = 0;
        int couponKey = 0;

        if (condition.getCouponId() == 0) { // insert
            int articleKey = articlesDao.saveArticle(condition.getIntro(), settingsProperties.getMallId());

            if (articleKey > 0) {
                coupon.setIntro_id(articleKey);
            } else {
                log.error("insert article fail");

                return 0;
            }

            coupon.setPicture(condition.getPicture());
            couponKey = couponDao.saveGiftCoupon(coupon);

            if (couponKey <= 0) {
                log.error("insert coupon fail");
                return 0;
            } else {
                condition.setCouponId(couponKey);
                result = couponKey;
            }

        } else { // update

            result = articlesDao.editArticle(coupon.getIntro_id(), condition.getIntro());

            if (result <= 0) {
                log.error("update article fail");
                return 0;
            }

            if (!StringUtils.isEmpty(condition.getPicture())) {
                coupon.setPicture(condition.getPicture());
            }

            result = couponDao.editGiftCoupon(coupon);
        }

        couponDao.deleteVerificationWideByCoupon(condition.getCouponId());

        log.info("shops == {{{###############" + condition.getShops() + "#########}}}");

        boolean isAllShops = false;

        if (!StringUtils.isEmpty(condition.getShops())) {
            // List<Map<String, Object>> list = (List<Map<String, Object>>) JsonPath.parse(condition.getShopsRule()).read("$", List.class);
            List<Integer> shopIds = ((List<Integer>) JsonPath.parse(condition.getShops()).read("$", List.class));

            if (shopIds == null || shopIds.isEmpty()) isAllShops = true;
            else {
                for (Integer id : shopIds) {
                    saveVerificationWide(condition.getCouponId(), id);
                }
            }
        } else isAllShops = true;

        if (isAllShops) {
            List<Shop> shopList = shopDao.getAllShopList(settingsProperties.getMallId());

            for (Shop shop : shopList) {
                saveVerificationWide(condition.getCouponId(), shop.getShop_id());
            }
        }

        return result;
    }

    private void saveVerificationWide(Integer couponId, Integer shopId) {
        VerificationWide vw = new VerificationWide();
        vw.setCoupon_id(couponId);
        vw.setShop_id(shopId);
        vw.setMall_id(settingsProperties.getMallId());
        couponDao.saveVerificationWide(vw);
    }

    @Override
    public List<ReceiveCouponInfo> getGiftCouponInfoList(GiftCouponQueryCondition condition) {
        return couponDao.getGiftCouponInfoList(condition);
    }

    @Override
    public ReceiveCouponInfo getGiftCouponInfoById(Integer couponId) {
        return couponDao.getGiftCouponInfoById(couponId);
    }

    @Override
    public int getGiftCouponsCount(GiftCouponQueryCondition condition) {
        return couponDao.getGiftCouponsCount(condition);
    }

    @Override
    public BigDecimal getCouponLogsSum(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.getCouponLogsSum(condition);
    }

    @Override
    public int getCouponLogsCount(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.multipleCouponLogsCount(condition);
    }

    @Override
    public List<Coupon> getCouponLogsList(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.multipleQuery(condition);
    }

    @Override
    public void print2Excel(List<Coupon> couponLogs, OutputStream out) {
        List<String> titles = Arrays.asList("会员名称","会员手机号","礼品券名称", "领取时间", "成本单价", "积分", "状态", "核销人", "核销人所属", "核销时间");
//        List<Tuple2<List<String>, CellStyle>> data = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        couponLogs.stream().forEach(couponLog-> {
            List<String> rowData = new ArrayList<>();

            rowData.add(couponLog.getMember_name());
            rowData.add(couponLog.getMember_mobile());
            rowData.add(couponLog.getCoupon_name());
            rowData.add(dateTimeUtils.getDataTimeString(couponLog.getReceive_date()));
            rowData.add(couponLog.getCost_price() == null ? "" : couponLog.getCost_price().toString());
            rowData.add(String.valueOf(couponLog.getRequired_points()));
            rowData.add(CouponStatus.valueOf(couponLog.getCoupon_status()).theName());
            rowData.add(couponLog.getVc_name());
            rowData.add(couponLog.getShop_name());
            rowData.add(dateTimeUtils.getDataTimeString(couponLog.getVerification_date()));

//            Tuple2<List<String>, CellStyle> tuple2 = new Tuple2<>(rowData, POICellStyle.CELL_NORMAL_CENTER);

            data.add(rowData);
        });

        generator.generate(out, titles, data, "礼品券记录");
    }

    @Override
    public List<ReceiveCouponInfo> getGiftCouponInfoListForRaffle(GiftCouponQueryCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.getGiftCouponInfoListForRaffle(condition);
    }

    @Override
    public String associationCoupons2Json(List<ReceiveCouponInfo> list) {
        List<Map<String, Object>> coupons = new ArrayList<>();

        for (ReceiveCouponInfo coupon : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", coupon.getCoupon_id());
            map.put("name", coupon.getCoupon_name());
            coupons.add(map);
        }

        String json;

        try {
            json = new ObjectMapper().writeValueAsString(coupons);
        } catch (JsonProcessingException e) {
            json = "[]";
        }

        return json;
    }
}
