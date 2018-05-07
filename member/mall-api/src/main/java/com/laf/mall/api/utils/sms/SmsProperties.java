package com.laf.mall.api.utils.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.mall.sms")
@Data
public class SmsProperties {

    private String url = "http://112.74.76.186:8030/service/httpService/httpInterface.do";

    private String username = "JSM41961";

    private String password = "5tjfzvne";

    private String veryCode = "upxg5dedtrk2";

    private String tempid = "JSM41961-0001";
}
