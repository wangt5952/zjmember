package com.laf.manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.manager.core.support.tuple.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * 核销商户
     */
    private String shops;

    /**
     * 二维码图片url
     */
    private String qr_code = "";

    /**
     * 二维码参数
     **/
    private String qrcode_param = "";

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
    private Date expiry_date_start;

    /**
     * 有效期end
     */
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
    private String intro;

    /**
     * 优惠券简介id
     */
    private int intro_id;

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
    private int receivedTotal;

    /**
     * 每日已领取张数
     */
    private int receivedDaily;

    /**
     * 已核销数
     */
    private int verification;

    /**
     * 成本价
     */
    private BigDecimal cost_price;

    /**
     * 优惠价
     */
    private BigDecimal discounted_price;
    /**
     * 搜索字段
     */
    private String text1;

    /**
     * 会员注册时间start
     */
    private Date reg_time_start;

    /**
     * 会员注册时间end
     */
    private Date reg_time_end;

    public List<Tuple2<Integer, String>> getVerificationTypes() {
        List<Tuple2<Integer, String>> types = new ArrayList<>();

//        types.add(new Tuple2<>(0, "不参与"));
//        types.add(new Tuple2<>(1, "所有商户"));
//        types.add(new Tuple2<>(2, "部分商户"));
        types.add(new Tuple2<>(2, "商户"));
        types.add(new Tuple2<>(3, "工作人员"));
        types.add(new Tuple2<>(4, "停车场"));

        return types;
    }

    public long getExpiry_date_start() {
        return expiry_date_start == null ? 0L : expiry_date_start.getTime();
    }

    public long getExpiry_date_end() {
        return expiry_date_end == null ? 0L : expiry_date_end.getTime();
    }

    public long getIssue_start() {
        return issue_start == null ? 0L : issue_start.getTime();
    }

    public long getIssue_end() {
        return issue_end == null ? 0L : issue_end.getTime();
    }

    public long getReg_time_start() {
        return reg_time_start == null ? 0L : reg_time_start.getTime();
    }

    public long getReg_time_end() {
        return reg_time_end == null ? 0L : reg_time_end.getTime();
    }

    public String getText1() {
        String typeName = "";
        if (coupon_type == 0) typeName = "领取型";
        else if (coupon_type == 1) typeName = "激活型";
        else if (coupon_type == 2) typeName = "推送型";
        else if (coupon_type == 3) typeName = "关联型";

        String range = "";
        if (verification_type == 0) range = "不参与";
        else if (verification_type == 1) range = "商户";
        else if (verification_type == 2) range = "商户";
        else if (verification_type == 3) range = "工作人员";
        else if (verification_type == 4) range = "停车场";

        return coupon_name + typeName + range;
    }
}
