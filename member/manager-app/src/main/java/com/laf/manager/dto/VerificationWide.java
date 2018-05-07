package com.laf.manager.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationWide {

    private int cvw_id;

    /**
     * 相关优惠券
     */
    private int coupon_id;

    /**
     * 相关店铺
     */
    private int shop_id;

    /**
     * 相关工作人员
     */
    private int vc_id;

    /**
     * 相关商场
     */
    private int mall_id;

}
