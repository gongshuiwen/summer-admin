package com.hzboiler.erp.core.exception;

import lombok.Getter;

/**
 * Custom business exception class, should be constructed by {@link BusinessExceptionEnum}
 * <p>
 * Simple usage example:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST);
 * </pre></blockquote>
 * <p>
 * Custom message example:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST, "error message");
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
}