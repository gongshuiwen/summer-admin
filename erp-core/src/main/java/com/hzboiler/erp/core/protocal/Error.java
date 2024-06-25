package com.hzboiler.erp.core.protocal;

import com.hzboiler.erp.core.exception.BusinessExceptionEnum;
import lombok.Getter;

/**
 * Generic API Response Error.
 * <p>
 * Construct by builder, the simplest example:
 * <blockquote><pre>
 * Error.builder(CoreBusinessExceptionEnums.ERROR_TEST).build()
 * </pre></blockquote>
 * <p>
 * Construct by builder, with all parameters example:
 * <blockquote><pre>
 * Error.builder(CoreBusinessExceptionEnums.ERROR_TEST)
 *     .message("")
 *     .details(object)
 *     .stackTrace(stackTrace)
 *     .build()
 * </pre></blockquote>
 *
 * @author gongshuiwen
 * @see BusinessExceptionEnum
 */
@Getter
public class Error {

    private final String namespace;
    private final int code;
    private final String message;
    private final Object details;
    private final StackTraceElement[] stackTrace;

    // prevent external instantiation
    private Error(BusinessExceptionEnum businessExceptionEnum, String message, Object details, StackTraceElement[] stackTrace) {
        this.namespace = businessExceptionEnum.namespace();
        this.code = businessExceptionEnum.code();

        // If message is not null, use it to replace the default message of the businessExceptionEnum
        this.message = message != null ? message : businessExceptionEnum.message();

        this.details = details;
        this.stackTrace = stackTrace;
    }

    public static Builder builder(BusinessExceptionEnum businessExceptionEnum) {
        return new Builder(businessExceptionEnum);
    }

    public static class Builder {

        private final BusinessExceptionEnum businessExceptionEnum;
        private String message;
        private Object details;
        private StackTraceElement[] stackTrace;

        // prevent external instantiation
        private Builder(BusinessExceptionEnum businessExceptionEnum) {
            this.businessExceptionEnum = businessExceptionEnum;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(Object details) {
            this.details = details;
            return this;
        }

        public Builder stackTrace(StackTraceElement[] stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }

        public Error build() {
            return new Error(businessExceptionEnum, message, details, stackTrace);
        }
    }
}
