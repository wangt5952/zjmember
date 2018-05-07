package com.laf.manager.dto;

import com.laf.manager.enums.CouponStatus;
import com.laf.manager.enums.CouponType;
import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Coupon {

    /**
     * 优惠券id(主键)
     */
    private int crl_id;

    /**
     * 相关优惠券
     */
    private int coupon_id;

    /**
     * 优惠券类型
     */
    private int coupon_type;
    private String type_name;

    /**
     * 成本价
     */
    private BigDecimal cost_price;

    /**
     * 所需积分
     */
    private int required_points;

    /**
     * 相关会员
     */
    private int member_id;

    /**
     * 会员名称
     */
    private String member_name;

    /**
     * 会员联系电话
     */
    private String member_mobile;

    /**
     * 核销人名称
     */
    private String vc_name;

    /**
     * 核销人电话
     */
    private String vc_mobile;

    private int shop_id;

    /**
     * 核销人所属
     */
    private String shop_name;

    /**
     * 核销时间
     */
    private Date verification_date;

    /**
     * 核销日志主键id
     */
    private int vlog_id;

    /**
     * 所属商城
     */
    private int mall_id;

    /**
     * 优惠券领取时间
     */
    private Date receive_date;

    /**
     * 优惠券状态 0:未激活 1:已激活 2:已核销
     */
    private int coupon_status;
    private String status_name;

    /**
     * 优惠券名称
     */
    private String coupon_name;

    /**
     * 有效期start
     */
    private Date expiry_date_start;

    /**
     * 有效期end
     */
    private Date expiry_date_end;

    /**
     * 是否过期 0:没过期；1:过期
     */
    private int past;

    public long getReceive_date() {
        return receive_date == null ? 0L : receive_date.getTime();
    }

    public long getExpiry_date_start() {
        return expiry_date_start == null ? 0L : expiry_date_start.getTime();
    }

    public long getExpiry_date_end() {
        return expiry_date_end == null ? 0L : expiry_date_end.getTime();
    }

    public long getVerification_date() {
        return verification_date == null ? 0L : verification_date.getTime();
    }

    public int getPast() {
        DateTimeUtils utils = new DateTimeUtils();
        long today = utils.getMilliByToDay();

        if (today <= this.getExpiry_date_end()) return past = 0;
        else                                   return past = 1;
    }

    public String getStatus_name() {
        return CouponStatus.valueOf(coupon_status).theName();
    }

    public String getType_name() {
        return CouponType.valueOf(coupon_type).theName();
    }

    public String getStatusName(int couponType) {
        if (coupon_status == CouponStatus.ACTIVATED.value() && couponType == CouponType.ACTIVITY.value()) {
            return "已激活";
        } else {
            return CouponStatus.valueOf(coupon_status).theName();
        }
    }

    public BigDecimal getCost_price() {
        return cost_price == null ? new BigDecimal(0L) : cost_price;
    }
}

