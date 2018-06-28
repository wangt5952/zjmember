package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GiftCoupon {
    public interface CouponAppListView {
    }

    public interface CouponDetailView extends CouponAppListView {
    }

    /**
     * 礼品券id(主键)
     */
    @JsonView(CouponAppListView.class)
    private int crl_id;

    /**
     * 相关礼品券
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
     * 礼品券领取时间
     */
    @JsonView(CouponAppListView.class)
    private Date receive_date;

    /**
     * 礼品券状态 1:已领取 2:已核销
     */
    @JsonView(CouponAppListView.class)
    private int coupon_status;

    /**
     * 礼品券名称
     */
    @JsonView(CouponAppListView.class)
    private String coupon_name;

    /**
     * 礼品券图片
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

    /**
     * 保留核销数
     */
    private int keep_verification_of;

    /**
     * 总核销数
     */
    private int verification_of;

    /**
     * 礼品券详细信息
     */
    @JsonView(CouponDetailView.class)
    private String intro;

    public long getReceive_date() {
        return receive_date == null ? 0L : receive_date.getTime();
    }

    public long getExpiry_date_start() {
        return expiry_date_start == null ? 0L : expiry_date_start.getTime();
    }

    public long getExpiry_date_end() {
        return expiry_date_end == null ? 0L : expiry_date_end.getTime();
    }
}

