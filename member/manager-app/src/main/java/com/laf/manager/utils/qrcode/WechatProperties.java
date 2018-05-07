package com.laf.manager.utils.qrcode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.laf.wechat")
@Data
public class WechatProperties {

    private String domain;
    private String path;
}
