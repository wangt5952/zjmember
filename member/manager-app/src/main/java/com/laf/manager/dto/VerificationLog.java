package com.laf.manager.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VerificationLog {

    /**
     * 核销日志id
     */
    private int vlog_id;

    /**
     * 相关会员
     */
    private int member_id;

    /**
     * 会员电话
     */
    private String member_mobile;

    /**
     * 会员姓名
     */
    private String member_name;

    /**
     * 相关优惠券名称
     */
    private String coupon_name;

    /**
     * 核销时间
     */
    private Date verification_date;

    /**
     * 核销人员id(主键)
     */
    private int vc_id;

    /**
     * 核销人员姓名
     */
    private String vc_name;

    /**
     * 核销人员电话
     */
    private String vc_phone;

    /**
     * 相关商户id
     */
    private int shop_id;

    /**
     * 商户名称
     */
    private String shop_name;

    /**
     * 相关的某张优惠券
     */
    private int crl_id;

    public long getVerification_date() {
        return verification_date == null ? 0L : verification_date.getTime();
    }
}
