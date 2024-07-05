package com.hzboiler.erp.core.protocal;

import com.hzboiler.erp.core.exception.BusinessExceptionEnum;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

/**
 * Generic API Response Result.
 * <p>
 * Construct success result example:
 * <blockquote><pre>
 * Result.success(data);
 * </pre></blockquote>
 * <p>
 * Construct error result with {@code BusinessExceptionEnum} example:
 * <blockquote><pre>
 * Result.error(CoreBusinessExceptionEnums.ERROR_TEST);
 * </pre></blockquote>
 * <p>
 * Construct error result with {@code BusinessExceptionEnum} and custom message example:
 * <blockquote><pre>
 * Result.error(CoreBusinessExceptionEnums.ERROR_TEST, "message to replace the default");
 * </pre></blockquote>
 * <p>
 * Construct error result with custom {@code Error} :
 * <blockquote><pre>
 * Result.error(
 *     Error.builder(CoreBusinessExceptionEnums.ERROR_TEST)
 *         .message("message to replace the default")
 *         .details(object)
 *         .stackTrace(stackTrace)
 *         .build()
 * );
 * </pre></blockquote>
 *
 * @param <T> The type of the data.
 * @author gongshuiwen
 * @see BusinessExceptionEnum
 * @see Error
 */
@Getter
public final class Result<T> {

    private final T data;
    private final Error error;
    private final Map<String, String> meta;

    // prevent external instantiation
    private Result(T data, Error error, Map<String, String> meta) {
        this.data = data;
        this.error = error;
        this.meta = meta;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, null);
    }

    public static <T> Result<T> error(BusinessExceptionEnum businessExceptionEnum) {
        Error error1 = Error.builder(businessExceptionEnum).build();
        return new Result<>(null, error1, null);
    }

    public static <T> Result<T> error(BusinessExceptionEnum businessExceptionEnum, String message) {
        Error error1 = Error.builder(businessExceptionEnum).message(message).build();
        return new Result<>(null, error1, null);
    }

    public static <T> Result<T> error(Error error) {
        Objects.requireNonNull(error);
        return new Result<>(null, error, null);
    }
}