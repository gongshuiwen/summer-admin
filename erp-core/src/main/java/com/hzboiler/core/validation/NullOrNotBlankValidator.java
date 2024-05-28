package com.hzboiler.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author gongshuiwen
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || !value.isBlank();
    }
}
