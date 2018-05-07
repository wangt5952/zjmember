package com.laf.manager.core.support;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleResponse {
    public SimpleResponse(Serializable content) {
        this.content = content;
    }

    public SimpleResponse(int code, Serializable content) {
        this.code = code;
        this.content = content;
    }

    private int code = 0;

    private Serializable content;
}
