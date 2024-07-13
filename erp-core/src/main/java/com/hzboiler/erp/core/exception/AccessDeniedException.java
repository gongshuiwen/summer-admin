package com.hzboiler.erp.core.exception;

/**
 * Generic access denied exception to be thrown when access is denied.
 *
 * @author gongshuiwen
 */
public class AccessDeniedException extends BusinessException {

    public AccessDeniedException(String message) {
        super(CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED, message);
    }
}
