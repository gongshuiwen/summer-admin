package io.summernova.admin.core.exception;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Record class {@code BusinessExceptionEnum} is used to globally define business exception constants,
 * which can be used to construct {@link BusinessException}.
 * <p>
 * Define example:
 * <blockquote><pre>
 * public class CoreBusinessExceptionEnums {
 *     public static final String NAME_SPACE = "core";
 *     public static final BusinessExceptionEnum ERROR_TEST = new BusinessExceptionEnum(NAME_SPACE, 99999, "This error is for test!");
 *     // prevent instantiation
 *     private BusinessExceptionEnums() {}
 * }</pre></blockquote>
 * <p>
 * Usage example:
 * <blockquote><pre>
 * throw new BusinessException(CoreBusinessExceptionEnums.ERROR_TEST);
 * </pre></blockquote>
 * <p>
 * Or use static import:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST);
 * </pre></blockquote>
 *
 * @param namespace exception namespace
 * @param code      exception code
 * @param message   exception message
 * @author gongshuiwen
 * @see BusinessException
 * @see CoreBusinessExceptionEnums
 */
public record BusinessExceptionEnum(String namespace, int code, String message) {

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

    private record Key(String namespace, int code) {
    }
}
