package io.summernova.admin.common.protocal;

import io.summernova.admin.common.exception.BusinessExceptionEnum;
import lombok.Getter;

import java.util.Objects;

/**
 * Generic API Response Error.
 * <p>
 * Construct by builder, the simplest example:
 * <blockquote><pre>
 * Error.builder(CoreBusinessExceptionEnums.ERROR_TEST).build()
 * </pre></blockquote>
 * <p>
 * Construct by builder, the all parameters example:
 * <blockquote><pre>
 * Error.builder(CoreBusinessExceptionEnums.ERROR_TEST)
 *     .message("message to replace the default")
 *     .details(object)
 *     .stackTrace(stackTrace)
 *     .build()
 * </pre></blockquote>
 *
 * @author gongshuiwen
 * @see BusinessExceptionEnum
 */
@Getter
public final class Error {

    private final String namespace;
    private final int code;
    private final String message;
    private final Object details;
    private final StackTraceElement[] stackTrace;

    // prevent external instantiation
    private Error(String namespace, int code, String message, Object details, StackTraceElement[] stackTrace) {
        this.namespace = namespace;
        this.code = code;
        this.message = message;
        this.details = details;
        this.stackTrace = stackTrace;
    }

    public static Builder builder(BusinessExceptionEnum businessExceptionEnum) {
        Objects.requireNonNull(businessExceptionEnum);
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

            // use the message of the businessExceptionEnum as the default message
            this.message = businessExceptionEnum.message();
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
            return new Error(businessExceptionEnum.namespace(), businessExceptionEnum.code(), message, details, stackTrace);
        }
    }
}
