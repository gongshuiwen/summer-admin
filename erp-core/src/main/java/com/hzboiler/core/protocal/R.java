package com.hzboiler.core.protocal;

import com.hzboiler.core.exception.BusinessExceptionEnum;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class R<T>{

    public final static int SUCCESS_CODE = 10000;
    public final static String SUCCESS_MESSAGE = "OK";

    private final int code;
    private final String message;
    private final T data;

    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success(T data) {
        return new R<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> error(BusinessExceptionEnum businessException) {
        return new R<>(businessException.code(), businessException.message(), null);
    }

    public static <T> R<T> error(BusinessExceptionEnum businessException, T data) {
        return new R<>(businessException.code(), businessException.message(), data);
    }
}