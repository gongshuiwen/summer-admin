package io.summernova.admin.core.exception;

import lombok.Getter;

/**
 * Custom business exception class, should be constructed by {@link BusinessExceptionEnum}
 * <p>
 * Simple usage:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST);
 * </pre></blockquote>
 * <p>
 * Custom message:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST, "error message");
 * </pre></blockquote>
 * <p>
 * Custom cause:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST, cause);
 * </pre></blockquote>
 * <p>
 * Custom message and cause:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST, "error message", cause);
 * </pre></blockquote>
 *
 * @author gongshuiwen
 * @see BusinessExceptionEnum
 */
@Getter
public class BusinessException extends RuntimeException {

    private final BusinessExceptionEnum businessExceptionEnum;

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        super(exceptionEnum.message());
        this.businessExceptionEnum = exceptionEnum;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum, String message) {
        super(message);
        this.businessExceptionEnum = exceptionEnum;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum, Throwable cause) {
        super(cause);
        this.businessExceptionEnum = exceptionEnum;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum, String message, Throwable cause) {
        super(message, cause);
        this.businessExceptionEnum = exceptionEnum;
    }
}