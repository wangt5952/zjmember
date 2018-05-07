package com.laf.mall.api;

import com.laf.mall.api.utils.coupon.CouponProperties;
import com.laf.mall.api.utils.file.FileProperties;
import com.laf.mall.api.utils.sms.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SmsProperties.class,
        FileProperties.class,
        CouponProperties.class
})
public class MallApiConfiguration {
}
