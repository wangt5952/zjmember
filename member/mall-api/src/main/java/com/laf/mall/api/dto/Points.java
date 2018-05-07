package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Points {

    public interface PointsAppView{};

    /**
     * 积分明细id 主键
     */
    @JsonView(PointsAppView.class)
    @ApiModelProperty(value = "积分记录ID")
    private String mplog_id;

    /**
     * 相关商城
     */
    private int mall_id;

    /**
     * 会员id
     */
    private int member_id;

    /**
     * 会员名称
     */
    private String member_name;

    /**
     * 会员手机
     */
    private String member_mobile;

    /**
     * 金额
     */
    @JsonView(PointsAppView.class)
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 积分
     */
    @JsonView(PointsAppView.class)
    private int points;

    /**
     * 相关商户
     */
    private int shop_id;

    /**
     * 商户名称
     */
    @JsonView(PointsAppView.class)
    @ApiModelProperty(value = "相关商户")
    private String shop_name;

    /**
     * 消费时间
     */
    @JsonView(PointsAppView.class)
    @ApiModelProperty(value = "消费时间")
    private Date shopping_date;

    /**
     * 消费单号
     */
    private String ticket_no;

    /**
     * 相关工作人员
     */
    private int vc_id;

    /**
     * 工作人员名称
     */
    private String vc_name;

    /**
     * 操作时间
     */
    private Date handle_date;

    /**
     * 积分来源
     */
    @JsonView(PointsAppView.class)
    @ApiModelProperty(value = "积分来源")
    private int sources;

    public long getShopping_date() {
        return shopping_date == null ? 0L : shopping_date.getTime();
    }

    public long getHandle_date() {
        return handle_date == null ? 0L : handle_date.getTime();
    }

}
