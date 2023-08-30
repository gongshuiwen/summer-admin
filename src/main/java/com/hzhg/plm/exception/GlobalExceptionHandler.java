package com.hzhg.plm.exception;

import com.hzhg.plm.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final int INTERNAL_ERROR_CODE = 99999;
    private static final String INTERNAL_ERROR_MESSAGE = "Server internal error!";
    private static final String BUSINESS_ERROR_MESSAGE_TEMPLATE = "Business Error: [%d] %s";

    @ExceptionHandler(BusinessException.class)
    public R<String> businessExceptionHandler(BusinessException e) {
        int code = e.getCode();
        String message = e.getMessage();
        log.warn(String.format(BUSINESS_ERROR_MESSAGE_TEMPLATE, code, message));
        return R.error(code, message);
    }

    @ExceptionHandler(Exception.class)
    public R<String> commonExceptionHandler(Exception e) {
        log.error(e.toString(), e);
        return R.error(INTERNAL_ERROR_CODE, INTERNAL_ERROR_MESSAGE);
    }
}
