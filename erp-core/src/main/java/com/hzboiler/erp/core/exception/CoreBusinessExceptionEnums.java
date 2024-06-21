package com.hzboiler.erp.core.exception;

/**
 * Static utility class to define the {@link BusinessExceptionEnum} constants of namespace 'core'.
 *
 * @author gongshuiwen
 * @see BusinessExceptionEnum
 */
public final class CoreBusinessExceptionEnums {

    public static final String CORE_NAME_SPACE = "core";

    public static final BusinessExceptionEnum ERROR_AUTHENTICATION_FAILED = new BusinessExceptionEnum(CORE_NAME_SPACE, 80001, "身份认证失败！");
    public static final BusinessExceptionEnum ERROR_ACCESS_DENIED = new BusinessExceptionEnum(CORE_NAME_SPACE, 80002, "权限验证失败，无当前资源的访问权限！");

    public static final BusinessExceptionEnum ERROR_TEST = new BusinessExceptionEnum(CORE_NAME_SPACE, 90000, "This error is for test!");
    public static final BusinessExceptionEnum ERROR_API_NOT_IMPLEMENTED = new BusinessExceptionEnum(CORE_NAME_SPACE, 90001, "API is not implemented!");
    public static final BusinessExceptionEnum ERROR_API_DEPRECATED = new BusinessExceptionEnum(CORE_NAME_SPACE, 90002, "API is deprecated!");
    public static final BusinessExceptionEnum ERROR_INVALID_ARGUMENTS = new BusinessExceptionEnum(CORE_NAME_SPACE, 90003, "Invalid arguments!");
    public static final BusinessExceptionEnum ERROR_INVALID_REQUEST_BODY = new BusinessExceptionEnum(CORE_NAME_SPACE, 90004, "Invalid request body!");
    public static final BusinessExceptionEnum ERROR_VALIDATION_FAILED = new BusinessExceptionEnum(CORE_NAME_SPACE, 90005, "Validation failed!");

    public static final BusinessExceptionEnum ERROR_INTERNAL = new BusinessExceptionEnum(CORE_NAME_SPACE, 99999, "Server internal error!");

    // prevent external instantiation
    private CoreBusinessExceptionEnums() {
    }
}
