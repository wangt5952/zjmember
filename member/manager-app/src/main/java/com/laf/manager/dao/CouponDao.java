package com.laf.manager.dao;

import com.laf.manager.dto.*;
import com.laf.manager.querycondition.coupon.*;
import com.laf.manager.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class CouponDao {

    @Autowired
    CouponRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int saveReceiveCoupon(ReceiveCouponInfo coupon) {
        return repository.insertReceiveCoupon(coupon);
    }

    public int saveParkingCoupon(ReceiveCouponInfo coupon) {
        return repository.insertParkingCoupon(coupon);
    }

    public int editReceiveCoupon(ReceiveCouponInfo coupon) {
        return repository.updateReceiveCoupon(coupon);
    }

    public int editParkingCoupon(ReceiveCouponInfo coupon) {
        return repository.updateParkingCoupon(coupon);
    }

    public int deleteCoupon(final Integer couponId) {
        return repository.deleteCoupon(couponId);
    }

    public int updateCouponState(final Integer couponId,final Integer state) {
        return repository.updateCouponState(couponId,state);
    }

    public ReceiveCouponInfo getCouponInfoById(final Integer couponId) {
        return repository.selectCouponInfoById(couponId);
    }

    public ReceiveCouponInfo getParkingCouponInfoById(final Integer couponId) {
        return repository.selectParkingCouponInfoById(couponId);
    }

    public List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition) {
        return repository.selectCouponInfoList(condition);
    }

    public List<ReceiveCouponInfo> getParkingCouponInfoList(CouponQueryCondition condition) {
        return repository.selectParkingCouponInfoList(condition);
    }

    public List<VerificationWide> getVerificationWideByCouponId(final int couponId, final int verificationType) {
        return repository.selectVerificationWideByCouponId(couponId, verificationType);
    }

    public int deleteVerificationWide(final int cvwId) {
        return repository.deleteVerificationWide(cvwId);
    }

    public int saveVerificationWide(VerificationWide vw) {
        return repository.insertVerificationWide(vw);
    }

    public int deleteVerificationWideByCoupon(Integer couponId) {
        return repository.deleteVerificationWideByCoupon(couponId);
    }

    public int getCouponsCount(final CouponQueryCondition condition) {
        return repository.selectCouponsCount(condition);
    }

    public int getParkingCouponsCount(final CouponQueryCondition condition) {
        return repository.selectParkingCouponsCount(condition);
    }

    public List<Coupon> getReceivedLogList(ReceivedQueryCondition condition) {
        List<Coupon> list = repository.selectReceivedCouponList(condition);

        for (Coupon coupon : list) {
            int vlogId = coupon.getVlog_id();
            if (vlogId > 0) {
                String sql = "select verification_date, vl.vc_id, vc_name, vc.phone, vl.shop_id, shop_name" +
                        " from `T_VERIFICATION_LOG` vl left join `T_VERIFICATION_CLERK` vc on vl.vc_id=vc.vc_id" +
                        " left join `T_SHOP` s on vl.shop_id=s.shop_id where vlog_id=?";

                VerificationLog log = jdbcTemplate.query(sql, new Object[]{vlogId}, new int[]{Types.INTEGER}, new ResultSetExtractor<VerificationLog>() {
                    @Override
                    public VerificationLog extractData(ResultSet rs) throws SQLException, DataAccessException {
                        VerificationLog _log = null;

                        if (rs.next()) {
                            _log = new VerificationLog();
                            _log.setVerification_date(new Date(rs.getLong("verification_date")));
                            _log.setVc_id(rs.getInt("vc_id"));
                            _log.setVc_name(rs.getString("vc_name"));
                            _log.setVc_phone(rs.getString("phone"));
                            _log.setShop_id(rs.getInt("shop_id"));
                            _log.setShop_name(rs.getString("shop_name"));
                        }
                        return _log;
                    }
                });

                if (log != null) {
                    coupon.setVerification_date(new Date(log.getVerification_date()));
                    coupon.setVc_name(log.getVc_name());
                    coupon.setVc_mobile(log.getVc_phone());
                    coupon.setShop_name(log.getShop_name());
                }
            }
        }
        return list;
    }

    public List<VerificationLog> getVerificationLogList(LogQueryCondition condition) {
        return repository.selectVerificationLogList(condition);
    }

    public int getReceivedLogCount(ReceivedQueryCondition condition) {
        return repository.selectReceivedCouponCount(condition);
    }

    public int getVerificationLogCount(LogQueryCondition condition) {
        return repository.selectVerificationLogCount(condition);
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

    public List<ParkingCouponInfo> multipleParkingQuery(final CouponLogFilterCondition condition) {
        List<ParkingCouponInfo> $ = null;

        try {
            $ = repository.multipleSelectParkingCouponLogs(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public List<ParkingCouponInfo> multipleParkingPayQuery(final CouponLogFilterCondition condition) {
        List<ParkingCouponInfo> $ = null;

        try {
            $ = repository.multipleSelectParkingCouponPayLogs(condition);
        } catch (EmptyResultDataAccessException e) {
            $ = new ArrayList<>();
        }

        return $;
    }

    public int multipleCouponLogsCount(final CouponLogFilterCondition condition) {
        return repository.multipleSelectCouponLogsCount(condition);
    }

    public int multipleParkingCouponLogsCount(final CouponLogFilterCondition condition) {
        return repository.multipleSelectParkingCouponLogsCount(condition);
    }

    public int multipleParkingCouponPayLogsCount(final CouponLogFilterCondition condition) {
        return repository.multipleSelectParkingCouponPayLogsCount(condition);
    }

    public BigDecimal getCouponLogsSum(CouponLogFilterCondition condition) {
        return repository.selectCouponLogsSum(condition);
    }

    public BigDecimal getParkingCouponLogsSum(CouponLogFilterCondition condition) {
        return repository.selectParkingCouponLogsSum(condition);
    }

    public int saveCouponsForPush2Members(final List<Member> members, final int coupon_id) {
        return repository.insertCouponsForPush2Members(members, coupon_id);
    }

    public List<ReceiveCouponInfo> getCouponInfoListByType(CouponQueryCondition condition) {
        return repository.selectCouponInfoListByType(condition);
    }

    public List<Integer> getCouponCountMonthly(String year, int mallId) {
        return repository.selectCouponCountMonthly(year, mallId);
    }

    public List<Integer> getVerifCountMonthly(String year, int mallId) {
        return repository.selectVerifCountMonthly(year, mallId);
    }


    public int saveCouponInfoQRCode(final int couponId, final String url, final String qrCodeParam) {
        return repository.updateCouponInfoQRCode(couponId, url, qrCodeParam);
    }

    public int saveParkingCouponInfoQRCode(final int couponId, final String url, final String qrCodeParam) {
        return repository.updateParkingCouponInfoQRCode(couponId, url, qrCodeParam);
    }



    public int insertParkingInfo(String content){
        return repository.insertParkingInfo(content, 1);
    }

    public ParkingInfo selectParkingInfo(){
        return repository.selectParkingInfo();
    }

}
