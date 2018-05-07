package com.laf.manager.core.properties;

import lombok.Data;

@Data
public class SmsCodeProperties {

    private int length = 6;

    private int expireIn = 20 * 60;

    private String url;
}
