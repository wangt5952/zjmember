package com.laf.manager.support;


import java.io.Serializable;

public class SimpleResponse {
    public SimpleResponse(Serializable content) {
        this.content = content;
    }

    private Serializable content;

    public Serializable getContent() {
        return content;
    }

    public void setContent(Serializable content) {
        this.content = content;
    }
}
