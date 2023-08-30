package com.hzhg.plm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessExceptionEnum {

    ERROR_TEST(90000, "This error is for test!"),
    ERROR_API_NOT_IMPLEMENTED(90001, "API is not implemented!"),
    ERROR_API_DEPRECATED(90002, "API is deprecated!"),
    ;

    private final int code;
    private final String message;

}
