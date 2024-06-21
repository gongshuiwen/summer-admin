package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.field.util.FieldUtils;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestFieldUtils {

    @Test
    void testCheckFieldExists() {
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "id"));
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "name"));
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "createTime"));
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "updateTime"));
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "createUser"));
        assertTrue(FieldUtils.checkFieldExists(Mock.class, "updateUser"));

        assertFalse(FieldUtils.checkFieldExists(Mock.class, "id2"));
        assertFalse(FieldUtils.checkFieldExists(Mock.class, "xxx"));
        assertFalse(FieldUtils.checkFieldExists(Mock.class, "  "));
        assertFalse(FieldUtils.checkFieldExists(Mock.class, ""));
        assertFalse(FieldUtils.checkFieldExists(Mock.class, null));

        assertThrows(NullPointerException.class, () -> FieldUtils.checkFieldExists(null, "id"));
    }
}
