package com.hzhg.plm.core.exception;

import com.hzhg.plm.core.protocal.R;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hzhg.plm.core.exception.BusinessExceptionEnum.*;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private static final String _BUSINESS_ERROR_MESSAGE_TEMPLATE = "Business Error: [%d] %s";

    @ExceptionHandler(BusinessException.class)
    public R<String> handleBusinessException(BusinessException e) {
        int code = e.getCode();
        String message = e.getMessage();
        log.warn(String.format(_BUSINESS_ERROR_MESSAGE_TEMPLATE, code, message));
        return R.error(code, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<String> handleAccessDeniedException(Exception e) {
        return R.error(ERROR_ACCESS_DENIED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R<List<String>> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return R.error(ERROR_INVALID_ARGUMENTS, errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return R.error(ERROR_INVALID_ARGUMENTS, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> handleHttpMessageNotReadableException(Exception e) {
        return R.error(ERROR_INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(Exception.class)
    public R<String> handleInternalException(Exception e) {
        log.error(e.toString(), e);
        return R.error(ERROR_INTERNAL);
    }
}
