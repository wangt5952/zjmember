package com.laf.manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReceiveCouponInfo extends BaseCouponInfo {
    public interface ReceiveCouponInfoAppListView extends BaseCouponInfoView {}

    /**
     * 限制次数
     */
    private int limited;

    /**
     * 日限制次数
     */
    private int daily_limited;

    /**
     * 保留核销数
     */
    private int keep_verification_of;

    /**
     * 优惠价
     */
    private BigDecimal discounted_price;

    /**
     * 所需积分
     */
    @JsonView(ReceiveCouponInfoAppListView.class)
    private int required_points;

    /**
     * 排序号
     */
    private int sort_id;

    /**
     * 优惠券领取方式 0:积分兑换 1:积分+现金 2:免费领取
     */
    @JsonView(ReceiveCouponInfoAppListView.class)
    private int receive_method;

    /**
     * 有效激活时间(起始)
     */
    private Date active_time;

    /**
     * 每人可激活数量
     */
    private int activable;

    /**
     * 已激活数
     */
    private int activate;

    /**
     * 激活条件
     */
    private String activation_condition;

    /**
     * 激活地点
     */
    private String activetion_site;

    public long getActive_time() {
        return active_time == null ? 0L : active_time.getTime();
    }
}
