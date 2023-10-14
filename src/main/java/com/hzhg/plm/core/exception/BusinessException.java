package com.hzhg.plm.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    final private int code;

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }
}