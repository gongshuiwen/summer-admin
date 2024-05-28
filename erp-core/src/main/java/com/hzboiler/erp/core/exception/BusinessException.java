package com.hzboiler.erp.core.exception;

import lombok.Getter;

/**
 * Custom business exception class with namespace and code, can be built by {@link BusinessExceptionEnum}
 * <p>
 * Usage example:
 * <blockquote><pre>
 * throw new BusinessException(ERROR_TEST);
 * </pre></blockquote>
 *
 * @see BusinessExceptionEnum
 * @author gongshuiwen
 */
@Getter
public class BusinessException extends RuntimeException {

    final private String namespace;
    final private int code;

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        super(exceptionEnum.message());
        this.namespace = exceptionEnum.namespace();
        this.code = exceptionEnum.code();
    }
}