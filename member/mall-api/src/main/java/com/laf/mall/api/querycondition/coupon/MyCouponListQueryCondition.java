package com.laf.mall.api.querycondition.coupon;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MyCouponListQueryCondition {

    /**
     * 商城id
     */
    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mallId;

    /**
     * 会员id
     */
    @NotNull(message = "会员id不能为空")
    @Min(value = 1, message = "会员id无效")
    private Integer memberId;

    /**
     * 优惠券使用状态
     */
    @NotNull(message = "优惠券使用状态不能为空")
    private Integer couponStatus;

    private int page = 1;

    private int size = 5;

    public int getOffset() {
        return (page - 1) * size;
    }
}
