package com.hzhg.plm.exception;

import lombok.Getter;

@Getter
public enum BusinessExceptionEnum {

    ERROR_API_DEPRECATED(99997, "API is deprecated!"),
    ERROR_API_NOT_IMPLEMENTED(99998, "API is not implemented!"),
    ERROR_TEST(99999, "This error is for test!")
    ;

    private final int code;
    private final String message;

    BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
