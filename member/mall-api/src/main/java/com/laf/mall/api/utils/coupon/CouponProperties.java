package com.laf.mall.api.utils.coupon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.coupon.relate")
@Data
public class CouponProperties {

    private boolean isrelated;

    private int couponid;
}
