package com.hzboiler.erp.core.field.util;

import com.hzboiler.erp.core.field.annotations.ReadOnly;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author gongshuiwen
 */
class TestReadOnlyUtil {

    static final List<Field> readonlyFields = new ArrayList<>();

    static {
        try {
            readonlyFields.add(Class2.class.getDeclaredField("field2"));
            readonlyFields.add(Class1.class.getDeclaredField("field1"));
            readonlyFields.add(BaseModel.class.getDeclaredField("createTime"));
            readonlyFields.add(BaseModel.class.getDeclaredField("updateTime"));
            readonlyFields.add(BaseModel.class.getDeclaredField("createUser"));
            readonlyFields.add(BaseModel.class.getDeclaredField("updateUser"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetReadOnlyFields() {
        Field[] readOnlyFields = ReadOnlyUtil.getReadOnlyFields(Class2.class);
        assertArrayEquals(readonlyFields.toArray(), readOnlyFields);
        assertTrue(Arrays.stream(readOnlyFields).allMatch((field) -> field.canAccess(new Class2())));
    }

    @Getter
    @Setter
    static class Class1 extends BaseModel {

        @ReadOnly
        private String field1;
    }

    @Getter
    @Setter
    static class Class2 extends Class1 {

        @ReadOnly
        private String field2;
    }
}
