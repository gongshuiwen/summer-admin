package com.hzhg.plm.core.protocal;

import com.hzhg.plm.core.exception.BusinessExceptionEnum;
import lombok.Data;

@Data
public class R<T>{

    public final static int SUCCESS_CODE = 10000;
    public final static String SUCCESS_MESSAGE = "OK";

    private int code;
    private String message;
    private T data;

    public static <T> R<T> success(T obj) {
        R<T> r = new R<>();
        r.setCode(SUCCESS_CODE);
        r.setMessage(SUCCESS_MESSAGE);
        r.setData(obj);
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> R<T> error(BusinessExceptionEnum businessException) {
        R<T> r = new R<>();
        r.setCode(businessException.getCode());
        r.setMessage(businessException.getMessage());
        return r;
    }
}