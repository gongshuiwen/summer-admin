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
