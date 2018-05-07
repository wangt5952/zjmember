package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.utils.datetime.DateTimeUtils;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Coupon {
    public interface CouponAppListView {
    }

    public interface CouponDetailView extends CouponAppListView {
    }

    /**
     * 优惠券id(主键)
     */
    @JsonView(CouponAppListView.class)
    private int crl_id;

    /**
     * 相关优惠券
     */
    @JsonView(CouponAppListView.class)
    private int coupon_id;

    /**
     * 相关会员
     */
    @JsonView(CouponAppListView.class)
    private int member_id;

    /**
     * 所属商城
     */
    @JsonView(CouponAppListView.class)
    private int mall_id;

    /**
     * 优惠券领取时间
     */
    @JsonView(CouponAppListView.class)
    private Date receive_date;

    /**
     * 优惠券状态 0:未激活 1:已激活 2:已核销
     */
    @JsonView(CouponAppListView.class)
    private int coupon_status;

    /**
     * 优惠券名称
     */
    @JsonView(CouponAppListView.class)
    private String coupon_name;

    /**
     * 优惠券图片
     */
    @JsonView(CouponAppListView.class)
    private String picture;

    /**
     * 有效期start
     */
    @JsonView(CouponAppListView.class)
    private Date expiry_date_start;

    /**
     * 有效期end
     */
    @JsonView(CouponAppListView.class)
    private Date expiry_date_end;

//    /**
//     * 是否过期 0:没过期；1:过期
//     */
//    @JsonView(CouponAppListView.class)
//    private int past;

    /**
     * 优惠券详细信息
     */
    @JsonView(CouponDetailView.class)
    private String intro;

    /**
     * 优惠券类型
     */
    @JsonView(CouponAppListView.class)
    private int coupon_type;

    /**
     * 有效激活时间(起始)
     */
    @JsonView(CouponAppListView.class)
    private Date active_time;

    /**
     * 每人可激活数量
     */
    @JsonView(CouponAppListView.class)
    private int activable;

    /**
     * 激活条件
     */
    @JsonView(CouponAppListView.class)
    private String activation_condition;

    /**
     * 激活地点
     */
    @JsonView(CouponAppListView.class)
    private String activetion_site;

    /**
     * 激活限制提示
     */
    @JsonView(CouponDetailView.class)
    private int limitPromptCode = -1;

    public long getReceive_date() {
        return receive_date == null ? 0L : receive_date.getTime();
    }

    public long getExpiry_date_start() {
        return expiry_date_start == null ? 0L : expiry_date_start.getTime();
    }

    public long getExpiry_date_end() {
        return expiry_date_end == null ? 0L : expiry_date_end.getTime();
    }

//    public int getPast() {
//        DateTimeUtils utils = new DateTimeUtils();
//        long today = utils.getMilliByToDay();
//
//        if (today <= expiry_date_end.getTime()) return past = 0;
//        else return past = 1;
//    }

    public long getActive_time() {
        return active_time == null ? 0L : active_time.getTime();
    }
}

