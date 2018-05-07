package com.laf.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.ArticlesDao;
import com.laf.manager.dao.CouponDao;
import com.laf.manager.dao.MemberDao;
import com.laf.manager.dao.ShopDao;
import com.laf.manager.dto.*;
import com.laf.manager.enums.CouponStatus;
import com.laf.manager.enums.CouponType;
import com.laf.manager.querycondition.coupon.*;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.service.CouponService;
import com.laf.manager.utils.datetime.DateTimeUtils;
import com.laf.manager.utils.file.FileProperties;
import com.laf.manager.utils.poi.Generator;
import com.laf.manager.utils.qrcode.QRCode;
import com.laf.manager.utils.qrcode.ZxingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponDao couponDao;

    @Autowired
    ArticlesDao articlesDao;

    @Autowired
    ShopDao shopDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    FileProperties fileProperties;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    Generator generator;


    @Override
    public int editCoupon(ReceiveCouponEditCondition condition) {
        ReceiveCouponInfo coupon = couponDao.getCouponInfoById(condition.getCouponId());

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
        coupon.setCoupon_type(condition.getCouponType());
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
        coupon.setActive_time(new Date(condition.getActiveTime()));
        coupon.setActivable(condition.getActivable());
        coupon.setActivetion_site(condition.getActivetionSite());
        coupon.setActivation_condition(condition.getActivationCondition());

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
            couponKey = couponDao.saveReceiveCoupon(coupon);

            if (couponKey <= 0) {
                log.error("insert coupon fail");
                return 0;
            } else {
                coupon.setCoupon_id(couponKey);
                coupon.setQrcode_param(condition.getQrCodeParam());
                createQRCode(coupon);
                couponDao.saveCouponInfoQRCode(couponKey, coupon.getQr_code(), coupon.getQrcode_param());

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

            coupon.setQrcode_param(condition.getQrCodeParam());
            createQRCode(coupon);
            result = couponDao.saveCouponInfoQRCode(coupon.getCoupon_id(), coupon.getQr_code(), coupon.getQrcode_param());

            if (result <= 0) {
                log.error("update qrcode fail");
                return 0;
            }
            result = couponDao.editReceiveCoupon(coupon);
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

    /**
     *
     * @param couponId
     * @param shopId
     */
    private void saveVerificationWide(Integer couponId, Integer shopId) {
        VerificationWide vw = new VerificationWide();
        vw.setCoupon_id(couponId);
        vw.setShop_id(shopId);
        vw.setMall_id(settingsProperties.getMallId());
        couponDao.saveVerificationWide(vw);
    }


    public ReceiveCouponInfo getReceiveCoupon(Integer couponId) {
        return couponDao.getCouponInfoById(couponId);
    }

    @Override
    public List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition) {
        return couponDao.getCouponInfoList(condition);
    }

    @Override
    public List<ReceiveCouponInfo> getCouponInfoListFromType(CouponType couponType) {
        CouponQueryCondition couponQueryCondition = new CouponQueryCondition();
        couponQueryCondition.setMallId(settingsProperties.getMallId());
        couponQueryCondition.setCouponType(couponType.value());
        couponQueryCondition.setSize(1000);

        return couponDao.getCouponInfoList(couponQueryCondition);
    }

    @Override
    public int delReceiveCoupon(Integer couponId) {
        int result = couponDao.deleteCoupon(couponId);
        if (result > 0) couponDao.deleteVerificationWideByCoupon(couponId);
        return result;
    }

    @Override
    public int getCouponsCount(final CouponQueryCondition condition) {
        return couponDao.getCouponsCount(condition);
    }

    @Override
    public List<Coupon> getReceivedLogList(CouponLogFilterCondition condition) {
        return this.getCouponLogsList(condition);
    }

    @Override
    public int getReceivedLogCount(CouponLogFilterCondition condition) {
        return this.getCouponLogsCount(condition);
    }

    @Override
    public List<Coupon> getVerificationLogList(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        condition.setOrder("verification_date");
        condition.setCouponStatus(CouponStatus.VERIFICATION.value());
        return couponDao.multipleQuery(condition);
    }

    @Override
    public int getVerificationLogCount(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        condition.setOrder("verification_date");
        condition.setCouponStatus(CouponStatus.VERIFICATION.value());
        return couponDao.multipleCouponLogsCount(condition);
    }

    @Override
    public List<Coupon> getCouponLogsList(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.multipleQuery(condition);
    }

    @Override
    public int getCouponLogsCount(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.multipleCouponLogsCount(condition);
    }

    @Override
    public BigDecimal getCouponLogsSum(CouponLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.getCouponLogsSum(condition);
    }

    @Override
    public int editPushCoupon(PushCouponEditCondition condition) {
        ReceiveCouponInfo coupon = couponDao.getCouponInfoById(condition.getCouponId());

        if (coupon == null) {
            coupon = new ReceiveCouponInfo();
        }

        coupon.setCoupon_name(condition.getCouponName());
        coupon.setPrice(condition.getPrice());
        coupon.setCost_price(condition.getCostPrice());
        coupon.setCoupon_type(condition.getCouponType());
        coupon.setExpiry_date_start(new Date(condition.getExpiryDateStart()));
        coupon.setExpiry_date_end(new Date(condition.getExpiryDateEnd()));
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
            coupon.setText1(condition.getCouponName() + condition.getIntro());
            couponKey = couponDao.saveReceiveCoupon(coupon);

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

            result = couponDao.editReceiveCoupon(coupon);
        }

        couponDao.deleteVerificationWideByCoupon(condition.getCouponId());

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

    public int pushCoupons(MemberFilterCondition condition, final String ids) throws MallDBException {
        int result = 0;
        List<Member> members = memberDao.getMultipleAllMembers(condition);
        String[] couponIds = ids.split(",");

        if (members.size() > 0 && couponIds.length > 0) {
            for (String id : couponIds) {
                result = couponDao.saveCouponsForPush2Members(members, Integer.valueOf(id));

                if (result <= 0) throw new MallDBException();
            }
        }

        return result;
    }

    @Override
    public List<ReceiveCouponInfo> getCouponInfoListByType(CouponQueryCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return couponDao.getCouponInfoListByType(condition);
    }

    @Override
    public List<Integer> getCouponCountMonthly(String year) {
        return couponDao.getCouponCountMonthly(year, settingsProperties.getMallId());
    }

    @Override
    public List<Integer> getVerifCountMonthly(String year) {
        return couponDao.getVerifCountMonthly(year, settingsProperties.getMallId());
    }

    private int createQRCode(ReceiveCouponInfo couponInfo) {
        QRCode qrCode = new QRCode();
        qrCode.setWidth(300);
        qrCode.setHeight(300);
        qrCode.setFormat("png");
        String content = StringUtils.isEmpty(couponInfo.getQrcode_param()) ? "" : couponInfo.getQrcode_param();
        qrCode.setContent(content + couponInfo.getCoupon_id());
        log.info("qrcode's content {}", qrCode.getContent());
        String folder = fileProperties.getPath();
        String fileName = "qr_coupon_" + new Date().getTime() + "." + qrCode.getFormat();
        qrCode.setFileName(fileName);
        qrCode.setFolder(folder);

        int result = ZxingUtils.createQRCode(qrCode);
        if (result != -1) {
            String url = fileProperties.getDomain() + fileProperties.getBase() + fileName;
            couponInfo.setQr_code(url);
        }
        return result;
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

    @Override
    public void print2Excel(List<Coupon> couponLogs, OutputStream out) {
        List<String> titles = Arrays.asList("会员名称","会员手机号","券名称","券类型", "领取时间", "成本单价", "积分", "状态", "核销人", "核销人所属", "核销时间");
//        List<Tuple2<List<String>, CellStyle>> data = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        couponLogs.stream().forEach(couponLog-> {
            List<String> rowData = new ArrayList<>();

            rowData.add(couponLog.getMember_name());
            rowData.add(couponLog.getMember_mobile());
            rowData.add(couponLog.getCoupon_name());
            rowData.add(CouponType.valueOf(couponLog.getCoupon_type()).theName());
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

        generator.generate(out, titles, data, "优惠券记录");
    }
}
