package com.laf.mall.api.querycondition.coupon;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CouponForShopQueryCondition {

    @NotNull(message = "商户id不能为空")
    @Min(value = 1, message = "商户id无效")
    private Integer shopId;

    @NotNull(message = "商场id不能为空")
    @Min(value = 1, message = "商场id无效")
    private Integer mallId;

    private Integer page = 1;

    @Min(value = 1, message = "size无效")
    private Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

}
