package com.laf.mall.api.dao;

import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.coupon.CouponForShopQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponQueryCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.repository.ParkingCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class ParkingCouponDao {

    @Autowired
    private ParkingCouponRepository repository;

    /**
     * 清除会员的默认车辆
     * @param memberId
     * @return
     */
    public int updateParking(final int memberId){
        return repository.updateParking(memberId);
    }

    /**
     * 设置会员的默认车辆
     * @param memberId
     * @param carNumber
     * @return
     */
    public int updateParking(final int memberId, String carNumber){
        return repository.updateParking(memberId,carNumber);
    }

    public int saveParking(final Parking parking){
        return repository.insertParking(parking);
    }

    public List<Parking> getParkingList(final int memberId){
        return repository.selectParkingByMemberId(memberId);
    }

    public List<Parking> getParkingList(final int memberId,String car_number){
        return repository.selectParkingByMemberId(memberId, car_number);
    }

    public List<ReceiveCouponInfo> getCouponInfoList(CouponQueryCondition condition, long registTime) {
        return repository.selectCouponInfoList(condition, registTime);
    }

    public ReceiveCouponInfo getCouponInfoById(final Integer couponId, final long today) {
        return repository.selectCouponInfoById(couponId, today);
    }

    public int getReceivedCountInCouponGroup(final Integer couponId, final Integer memberId, final Integer groupId) {
        return repository.selectReceivedCountInCouponGroup(couponId, memberId, groupId);
    }

    public int getReceivedByMember(final Integer memberId, final Integer couponId) {
        return repository.selectReceivedByMember(memberId, couponId);
    }

    public int getDailyReceivedByMember(final Integer memberId, final Integer couponId, final long currTime) {
        return repository.selectDailyReceivedByMember(memberId, couponId, currTime);
    }

    public int getNonVerificationCouponByMember(final Integer memberId, final Integer couponId) {
        return repository.selectNonVerificationCouponByMember(memberId, couponId);
    }

    public int saveReceivedCouponByMember(final CouponReceiveCondition condition) {
        return repository.insertReceivedCouponByMember(condition);
    }

    public List<Coupon> getCouponListByMember(final MyCouponListQueryCondition condition) {
        return repository.selectCouponListByMember(condition);
    }

    public Coupon getCouponById(final Integer crlId) {
        return repository.selectCouponById(crlId);
    }

    public int getVerificationWideByShop(VerificationClerk vc, final int coupon_id) {
        return repository.selectVerificationWideByShop(vc, coupon_id);
    }

    public int updateCouponStatus(final int status, final int crlId) {
        return repository.updateCouponStatus(status, crlId);
    }

    public int saveVerification(VerificationClerk vc, Coupon coupon) {
        return repository.insertVerification(vc, coupon);
    }

    public String getCouponRule(int mallId) {
        return repository.selectCouponRule(mallId);
    }

    public int getCouponCountByStatus(final int memberId, final int couponId, final int couponStatus) {
        return repository.selectCouponCountByStatus(memberId, couponId, couponStatus);
    }

    public int getCouponCountByStatus( final int couponId, final int couponStatus) {
        return repository.selectCouponCountByStatus(couponId, couponStatus);
    }

    public int saveCouponActiveLog(Activate activate) {
        return repository.insertCouponActiveLog(activate);
    }

    public List<ReceiveCouponInfo> getRelateCouponInfoList(final int mallId) {
        return repository.selectRelateCouponInfoList(mallId);
    }

    public List<ReceiveCouponInfo> getCouponInfoListByShopId(CouponForShopQueryCondition condition) {
        return repository.selectCouponInfoListByShopId(condition);
    }

    public ParkingInfo getParkingInfo(){
        return repository.selectParkingInfo();
    }

    public int saveParkingLog(int crlId, int memberId, String carNumber, BigDecimal price, Date inDate,String ticketNo) {
        return repository.insertParkingLog( crlId,  memberId,  carNumber,  price,  inDate, ticketNo);
    }
}
