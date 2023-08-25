package com.hzhg.plm.exception;

import com.hzhg.plm.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final int INTERNAL_EXCEPTION_CODE = 10000;

    @ExceptionHandler(BusinessException.class)
    public R<String> businessExceptionHandler(BusinessException e) {
        int code = e.getCode();
        String message = e.getMessage();
        log.info("BusinessException occurred: [" + code + "] " + message);
        return R.error(code, message);
    }

    @ExceptionHandler(Exception.class)
    public R<String> commonExceptionHandler(Exception e) {
        String message = e.getMessage();
        log.error(message);
        return R.error(INTERNAL_EXCEPTION_CODE, message);
    }
}
