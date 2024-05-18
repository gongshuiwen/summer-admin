package com.hzboiler.core.exception;

import lombok.Getter;

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