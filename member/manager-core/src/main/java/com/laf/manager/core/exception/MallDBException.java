package com.laf.manager.core.exception;

public class MallDBException extends RuntimeException {

    private int result;

    public MallDBException(int result) {
        super("DB Write In Exception");
        this.result = result;
    }

    public MallDBException() {
        this(0);
    }

    public int getResult() {
        return this.result;
    }
}
