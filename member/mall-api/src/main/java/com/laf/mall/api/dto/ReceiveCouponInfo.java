package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReceiveCouponInfo extends BaseCouponInfo {
    public interface ReceiveCouponInfoAppListView extends BaseCouponInfoView {}
    public interface ReceiveCouponInfoView extends ReceiveCouponInfoAppListView {}

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
     * 会员注册时间start
     */
    private Date reg_time_start;

    /**
     * 会员注册时间end
     */
    private Date reg_time_end;

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
    @JsonView(ReceiveCouponInfoAppListView.class)
    private Date active_time;

    /**
     * 每人可激活数量
     */
    @JsonView(ReceiveCouponInfoAppListView.class)
    private int activable;

    /**
     * 激活条件
     */
    @JsonView(ReceiveCouponInfoAppListView.class)
    private String activation_condition;

    /**
     * 激活地点
     */
    @JsonView(ReceiveCouponInfoAppListView.class)
    private String activetion_site;

    public long getActive_time() {
        return active_time == null ? 0L : active_time.getTime();
    }

    public String getActivation_condition() {
        return StringUtils.isEmpty(activation_condition) ? "" : activation_condition;
    }

    public String getActivetion_site() {
        return StringUtils.isEmpty(activetion_site) ? "" : activetion_site;
    }
}
