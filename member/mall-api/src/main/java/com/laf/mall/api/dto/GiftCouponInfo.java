package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GiftCouponInfo {
    public interface GiftCouponInfoListView{}
    public interface GiftCouponInfoView extends GiftCouponInfoListView{}

    /**
     * 优惠券id(主键)
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    private int coupon_id;

    /**
     * 优惠券名称
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    private String coupon_name;

    /**
     * 优惠券图片
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    private String picture;

    /**
     * 核销范围 0:不参与 1:所有商户 2:部分商户 3:工作人员 4:停车场
     */
    private int verification_type;

    /**
     * 所属商场id
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    private int mall_id;

    /**
     * 优惠券创建时间
     */
    private Date create_date;

    /**
     * 券发行开始时间
     */
    private Date issue_start;

    /**
     * 券发行结束时间
     */
    private Date issue_end;

    /**
     * 有效期start
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private Date expiry_date_start;

    /**
     * 有效期end
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private Date expiry_date_end;

    /**
     * 总发行量
     */
    private int circulation;

    /**
     * 日发行量
     */
    private int daily_circulation;

    /**
     * 总核销量
     */
    private int verification_of;

    /**
     * 市场价
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private BigDecimal price;

    /**
     * 优惠券描述
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private String intro;

    /**
     * 优惠券状态 0:草稿 1:发布
     */
    private int status;

    /**
     * 所属优惠券组(互斥用)，默认-1，表示不属于任何组
     */
    private int group_id;

    /**
     * 库存
     */
    private int inventory;

    /**
     * 优惠券使用条件
     */
    private BigDecimal use_condition;

    /**
     * 已领取总张数
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private int receivedTotal;

    /**
     * 每日已领取张数
     */
    private int receivedDaily;

    /**
     * 搜索字段
     */
    private String text1;

    /**
     * 领取限制提示
     */
    @JsonView(GiftCouponInfo.GiftCouponInfoView.class)
    private int limitPromptCode;

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
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
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
    @JsonView(GiftCouponInfo.GiftCouponInfoListView.class)
    private int receive_method;

    public String getIntro() {
        return StringUtils.isEmpty(intro) ? "" : intro;
    }

}
