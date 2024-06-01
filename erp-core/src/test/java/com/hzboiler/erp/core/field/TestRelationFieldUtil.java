package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.field.annotations.InverseField;
import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.util.ReflectUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TestRelationFieldUtil {

    @Getter
    @Setter
    static class Mock extends BaseModel {

        @InverseField("field2")
        private Many2One<Mock> field1;

        @InverseField("field10")
        private One2Many<Mock> field2;
        private Many2Many<Mock> field3;
        private Many2One<?> field4;
        private Long field5;
    }

    @Getter
    @Setter
    static class Mock2 extends Mock {
        private Many2One<Mock> field6;
        private One2Many<Mock> field7;
        private Many2Many<Mock> field8;
    }

    @Test
    void testGetTargetModelClass() throws NoSuchFieldException {
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field1")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field2")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field3")));

        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field4")));
        assertThrowsExactly(IllegalArgumentException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field5")));
    }

    @Test
    void testGetMany2OneFields() {
        Field[] many2OneFields = RelationFieldUtil.getMany2OneFields(Mock2.class);
        assertEquals(3, many2OneFields.length);
        assertArrayEquals(ReflectUtil.getAllDeclaredFieldsWithType(Mock2.class, Many2One.class), many2OneFields);
        assertTrue(Arrays.stream(many2OneFields).allMatch((field) -> field.canAccess(new Mock2())));
    }

    @Test
    void testGetOne2ManyFields() {
        Field[] one2ManyFields = RelationFieldUtil.getOne2ManyFields(Mock2.class);
        assertEquals(2, one2ManyFields.length);
        assertArrayEquals(ReflectUtil.getAllDeclaredFieldsWithType(Mock2.class, One2Many.class), one2ManyFields);
        assertTrue(Arrays.stream(one2ManyFields).allMatch((field) -> field.canAccess(new Mock2())));
    }

    @Test
    void testGetMany2ManyFields() {
        Field[] many2ManyFields = RelationFieldUtil.getMany2ManyFields(Mock2.class);
        assertEquals(2, many2ManyFields.length);
        assertArrayEquals(ReflectUtil.getAllDeclaredFieldsWithType(Mock2.class, Many2Many.class), many2ManyFields);
        assertTrue(Arrays.stream(many2ManyFields).allMatch((field) -> field.canAccess(new Mock2())));
    }

    @Test
    void testGetInverseField() throws NoSuchFieldException {
        Field inverseField = RelationFieldUtil.getInverseField(Mock.class.getDeclaredField("field1"));
        assertEquals(Mock.class.getDeclaredField("field2"), inverseField);

        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getInverseField(Mock.class.getDeclaredField("field2")));
        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getInverseField(Mock.class.getDeclaredField("field3")));
    }
}
