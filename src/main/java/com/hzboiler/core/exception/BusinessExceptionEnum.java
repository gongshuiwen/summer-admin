package com.hzboiler.core.exception;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Record class {@code BusinessExceptionEnum} is used to globally define business exception constants,
 * which can be used to build {@link BusinessException}.
 * <p>
 * Define example:
 * <blockquote><pre>
 * public class CoreBusinessExceptionEnums {
 *     public static final String NAME_SPACE = "core";
 *     public static final BusinessExceptionEnum ERROR_TEST = new BusinessExceptionEnum(NAME_SPACE, 99999, "This error is for test!");
 *     // prevent instantiation
 *     private BusinessExceptionEnums() {}
 * }</pre></blockquote>
 *
 * Usage example:
 * <blockquote><pre>
 * throw new BusinessException(BusinessExceptionEnums.ERROR_TEST);
 * </pre></blockquote>
 *
 * Or use static import:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST);
 * </pre></blockquote>
 *
 * @see CoreBusinessExceptionEnums
 * @author gongshuiwen
 *
 * @param namespace exception namespace
 * @param code exception code
 * @param message exception message
 */
public record BusinessExceptionEnum(String namespace, int code, String message) {

    private record Key(String namespace, int code) {}
    private static final Map<Key, BusinessExceptionEnum> REGISTRY = new ConcurrentHashMap<>();

    public static final String CORE_NAME_SPACE = "core";

    public static final BusinessExceptionEnum ERROR_AUTHENTICATION_FAILED = new BusinessExceptionEnum(CORE_NAME_SPACE, 80001, "身份认证失败！");
    public static final BusinessExceptionEnum ERROR_ACCESS_DENIED = new BusinessExceptionEnum(CORE_NAME_SPACE, 80002, "权限验证失败，无当前资源的访问权限！");

    public static final BusinessExceptionEnum ERROR_TEST = new BusinessExceptionEnum(CORE_NAME_SPACE, 90000, "This error is for test!");
    public static final BusinessExceptionEnum ERROR_API_NOT_IMPLEMENTED = new BusinessExceptionEnum(CORE_NAME_SPACE, 90001, "API is not implemented!");
    public static final BusinessExceptionEnum ERROR_API_DEPRECATED = new BusinessExceptionEnum(CORE_NAME_SPACE, 90002, "API is deprecated!");
    public static final BusinessExceptionEnum ERROR_INVALID_ARGUMENTS = new BusinessExceptionEnum(CORE_NAME_SPACE, 90003, "Invalid arguments!");
    public static final BusinessExceptionEnum ERROR_INVALID_REQUEST_BODY = new BusinessExceptionEnum(CORE_NAME_SPACE, 90004, "Invalid request body!");

    public static final BusinessExceptionEnum ERROR_INTERNAL = new BusinessExceptionEnum(CORE_NAME_SPACE, 99999, "Server internal error!");

    public BusinessExceptionEnum {
        Objects.requireNonNull(namespace, "namespace must not be null");
        Objects.requireNonNull(message, "message must not be null");
        Key key = new Key(namespace, code);
        if (REGISTRY.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate business exception enum: " + key);
        }

        REGISTRY.put(key, this);
    }
}
