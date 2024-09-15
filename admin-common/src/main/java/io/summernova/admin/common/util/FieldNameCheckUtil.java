package io.summernova.admin.common.util;

import java.util.regex.Pattern;

/**
 * @author gongshuiwen
 */
public final class FieldNameCheckUtil {

    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("[a-z0-9_]+");

    // prevent external instantiation
    private FieldNameCheckUtil() {
    }

    public static void checkFieldName(String fieldName) {
        if (fieldName == null)
            throw new IllegalArgumentException("The field name must not be null.");
        if (!FIELD_NAME_PATTERN.matcher(fieldName).matches())
            throw new IllegalArgumentException("The field name '" + fieldName + "' is not valid.");
    }
}
