package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GagBill {

    public interface GagListView{}

    /**
     * 返回状态码
     */
    @JsonView(GagListView.class)
    private String rescode;

    /**
     * 返回信息
     */
    @JsonView(GagListView.class)
    private String resmsg;

    /**
     * 账单id
     */
    @JsonView(GagListView.class)
    private String uuid;

    /**
     * 应收金额
     */
    @JsonView(GagListView.class)
    private BigDecimal amount;

    /**
     * 桌号
     */
    @JsonView(GagListView.class)
    private String deskno;

    /**
     * 商家小票流水号
     */
    @JsonView(GagListView.class)
    private String billserialnumber;

    /**
     *第三方商户代码
     */
    @JsonView(GagListView.class)
    private String merchantCode;

    /**
     *第三方商户名称
     */
    @JsonView(GagListView.class)
    private String merchantName;

    /**
     * 签名
     */
    @JsonView(GagListView.class)
    private String sign;

    /**
     * 销售日期
     */
    @JsonView(GagListView.class)
    private String saleTime;


}
