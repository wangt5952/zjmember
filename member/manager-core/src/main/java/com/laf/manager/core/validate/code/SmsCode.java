package com.laf.manager.core.validate.code;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class SmsCode extends ValidateCode {

    public SmsCode(String code, int expireIn) {
        super(code, LocalDateTime.now().plusSeconds(expireIn));
    }

}
