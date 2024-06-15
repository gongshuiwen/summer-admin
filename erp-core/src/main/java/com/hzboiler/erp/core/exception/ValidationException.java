package com.hzboiler.erp.core.exception;

import java.util.function.Supplier;

/**
 * A generic validation exception to be thrown when validation fails.
 *
 * @author gongshuiwen
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(CoreBusinessExceptionEnums.ERROR_VALIDATION_FAILED, message);
    }

    /**
     * Not commonly used, so don't waste time read this unless you are really free :)
     * <p>
     * Return false if throwsException is false, otherwise throw ValidationException with the given message.
     * <p>
     * When we write a validation method, sometimes we want to throw an exception if the check fails, sometimes we just want return false.
     * So we can use a boolean flag 'throwsException' to indicate the behavior like this:
     * <blockquote><pre>
     * boolean checkSomething(boolean throwsException) {
     *     if (someCheck1Fails) {
     *         if (throwsException)
     *             throw new ValidationException("error message1");
     *         return false;
     *     }
     *
     *     if (someCheck2Fails) {
     *         if (throwsException)
     *             throw new ValidationException("error message2" + someStrings);
     *         return false;
     *     }
     *     ...
     *     return true;
     * }
     * </pre></blockquote>
     * In this case, we can use this method like this:
     * <blockquote><pre>
     * boolean checkSomething(boolean throwsException) {
     *     if (someCheck1Fails)
     *         return ValidationException.returnFalseOrThrow(throwsException, "error1 message");
     *
     *     if (someCheck2Fails)
     *         return ValidationException.returnFalseOrThrow(throwsException, "error message2" + someStrings);
     *     ...
     *     return true;
     * }
     * </pre></blockquote>
     */
    public static boolean returnFalseOrThrow(boolean throwsException, String message) {
        if (throwsException)
            throw new ValidationException(message);
        return false;
    }

    /**
     * <p>
     * Same like {@link #returnFalseOrThrow(boolean, String)} but with {@link Supplier},
     * for lazily evaluating the message which needs to be computed dynamically.
     * <blockquote><pre>
     * boolean checkSomething(boolean throwsException) {
     *     if (someCheck1Fails)
     *         return ValidationException.returnFalseOrThrow(throwsException, () -> "error message1" + someStrings);
     *     ...
     *     return true;
     * }
     * </pre></blockquote>
     */
    public static boolean returnFalseOrThrow(boolean throwsException, Supplier<String> messageSupplier) {
        if (throwsException)
            throw new ValidationException(messageSupplier.get());
        return false;
    }
}
