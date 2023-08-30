package com.hzhg.plm.common;

import lombok.Data;

@Data
public class R<T>{

    private int code;
    private String message;
    private T data;

    public static <T> R<T> success(T obj) {
        R<T> r = new R<>();
        r.setCode(10000);
        r.setMessage("OK");
        r.setData(obj);
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}