package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseCouponInfo {
    public interface BaseCouponInfoView {}

    /**
     * 优惠券id(主键)
     */
    @JsonView(BaseCouponInfoView.class)
    private int coupon_id;

    /**
     * 优惠券名称
     */
    @JsonView(BaseCouponInfoView.class)
    private String coupon_name;

    /**
     * 优惠券图片
     */
    @JsonView(BaseCouponInfoView.class)
    private String picture;

    /**
     * 二维码图片地址
     */
    private String qr_code;

    /**
     * 核销范围 0:不参与 1:所有商户 2:部分商户 3:工作人员 4:停车场
     */
    private int verification_type;

//    /**
//     * 所属业态id
//     */
//    private int industries;

    /**
     * 所属商场id
     */
    @JsonView(BaseCouponInfoView.class)
    private int mall_id;

    /**
     * 优惠券类型 0:领取型 1:激活型 2:推送型 3:关联型
     */
    @JsonView(BaseCouponInfoView.class)
    private int coupon_type;

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
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
    private Date expiry_date_start;

    /**
     * 有效期end
     */
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
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
    @JsonView(BaseCouponInfoView.class)
    private BigDecimal price;

    /**
     * 优惠券描述
     */
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
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
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
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
    @JsonView(ReceiveCouponInfo.ReceiveCouponInfoView.class)
    private int limitPromptCode;

    public String getIntro() {
        return StringUtils.isEmpty(intro) ? "" : intro;
    }
}
