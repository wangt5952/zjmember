package com.laf.mall.api.querycondition.coupon;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MyCouponQueryCondition {

    /**
     * 商城id
     */
    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mallId;

    /**
     * 优惠券使用状态
     */
    @NotNull(message = "优惠券使用状态不能为空")
    private Integer couponStatus;

    private int page = 1;

    private int size = 5;

    public int getOffset() {
        if (page <= 0) return 0;
        else           return (page - 1) * size;
    }
}
