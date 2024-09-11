package io.summernova.admin.common.exception;

/**
 * Generic access denied exception to be thrown when access is denied.
 *
 * @author gongshuiwen
 */
public class AccessDeniedException extends BusinessException {

    public AccessDeniedException(String message) {
        super(CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED, message);
    }

    public AccessDeniedException(Throwable cause) {
        super(CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED, cause);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED, message, cause);
    }
}
