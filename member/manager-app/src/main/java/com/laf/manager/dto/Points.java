package com.laf.manager.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.manager.enums.Sources;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Points {

    /**
     * 积分明细id 主键
     */
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
    private BigDecimal amount;

    /**
     * 积分
     */
    private int points;

    /**
     * 相关商户
     */
    private int shop_id;

    /**
     * 商户名称
     */
    private String shop_name;

    private String shop_industry;

    /**
     * 消费时间
     */
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
    private int sources;
    private String sourcesName;

    private String desc;

    public long getShopping_date() {
        return shopping_date == null ? 0L : shopping_date.getTime();
    }

    public long getHandle_date() {
        return handle_date == null ? 0L : handle_date.getTime();
    }

    public String getSourcesName() {
        return Sources.valueOf(sources).theName();
    }
}
