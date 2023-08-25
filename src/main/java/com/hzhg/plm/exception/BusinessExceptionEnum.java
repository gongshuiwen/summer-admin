package com.hzhg.plm.exception;

import lombok.Getter;

@Getter
public enum BusinessExceptionEnum {

    ERROR_TEST(99999, "This error is for test!")
    ;

    private final int code;
    private final String message;

    BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
