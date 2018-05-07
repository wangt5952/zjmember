package com.laf.manager.core.validate.code;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 8225163709580398817L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
