package com.hzhg.plm.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessExceptionEnum {

    ERROR_AUTHENTICATION_FAILED(80001, "身份认证失败！"),
    ERROR_ACCESS_DENIED(80002, "权限验证失败，无当前资源的访问权限！"),

    ERROR_TEST(90000, "This error is for test!"),
    ERROR_API_NOT_IMPLEMENTED(90001, "API is not implemented!"),
    ERROR_API_DEPRECATED(90002, "API is deprecated!"),
    ERROR_INVALID_ARGUMENTS(90003, "Invalid arguments!"),
    ERROR_INTERNAL(99999, "Server internal error!"),
    ;

    private final int code;
    private final String message;

}
