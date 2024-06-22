package com.hzboiler.erp.core.field.util;

import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestFieldUtil {

    @Test
    void testCheckFieldExists() {
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "id"));
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "name"));
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "createTime"));
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "updateTime"));
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "createUser"));
        assertTrue(FieldUtil.checkFieldExists(Mock.class, "updateUser"));

        assertFalse(FieldUtil.checkFieldExists(Mock.class, "id2"));
        assertFalse(FieldUtil.checkFieldExists(Mock.class, "xxx"));
        assertFalse(FieldUtil.checkFieldExists(Mock.class, "  "));
        assertFalse(FieldUtil.checkFieldExists(Mock.class, ""));
        assertFalse(FieldUtil.checkFieldExists(Mock.class, null));

        assertThrows(NullPointerException.class, () -> FieldUtil.checkFieldExists(null, "id"));
    }
}
