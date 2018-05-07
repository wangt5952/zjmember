package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VerificationClerk {
    public interface VerificationClerkAppInfoView {}

    @JsonView(VerificationClerkAppInfoView.class)
    private int vc_id;

    /**
     * 核查员姓名
     */
    @JsonView(VerificationClerkAppInfoView.class)
    private String vc_name;

    /**
     * 核查员联系电话
     */
    private String phone;

    /**
     * 核查员类型，0:工作人员，1:商户核销人员
     */
    private int vc_type;

    /**
     * 部门，类型为工作人员时使用，0:客服中心,1:其它
     */
    private int department = 0;

    private int shop_id;

    private int mall_id;

    /**
     * 登记人会员身份信息
     */
    private int member_id;

    /**
     * 注册时间
     */
    private Date reg_date = new Date();

    /**
     * 是否是负责人
     */
    private boolean is_manager = false;

    /**
     * 核查员审核状态 0:审核通过 1:未审核 2:审核未通过
     */
    @JsonView(VerificationClerkAppInfoView.class)
    private int status = 1;

    public long getReg_date() {
        return reg_date.getTime();
    }
}
