package com.hzhg.plm.core.exception;

import com.hzhg.plm.core.protocal.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final int _INTERNAL_ERROR_CODE = 99999;
    private static final String _INTERNAL_ERROR_MESSAGE = "Server internal error!";
    private static final String _BUSINESS_ERROR_MESSAGE_TEMPLATE = "Business Error: [%d] %s";

    @ExceptionHandler(BusinessException.class)
    public R<String> businessExceptionHandler(BusinessException e) {
        int code = e.getCode();
        String message = e.getMessage();
        log.warn(String.format(_BUSINESS_ERROR_MESSAGE_TEMPLATE, code, message));
        return R.error(code, message);
    }

    @ExceptionHandler(Exception.class)
    public R<String> commonExceptionHandler(Exception e) {
        log.error(e.toString(), e);
        return R.error(_INTERNAL_ERROR_CODE, _INTERNAL_ERROR_MESSAGE);
    }
}
