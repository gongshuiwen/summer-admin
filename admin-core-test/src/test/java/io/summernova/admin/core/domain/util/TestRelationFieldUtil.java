package io.summernova.admin.core.domain.util;

import io.summernova.admin.common.util.ReflectUtil;
import io.summernova.admin.core.domain.annotations.Many2OneField;
import io.summernova.admin.core.domain.annotations.One2ManyField;
import io.summernova.admin.core.domain.field.Many2Many;
import io.summernova.admin.core.domain.field.Many2One;
import io.summernova.admin.core.domain.field.One2Many;
import io.summernova.admin.core.domain.model.BaseModel;
import io.summernova.admin.core.domain.model.BaseTreeModel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestRelationFieldUtil {

    @Test
    void testGetTargetModelClass() throws NoSuchFieldException {
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class, Mock.class.getDeclaredField("field1")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class, Mock.class.getDeclaredField("field2")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class, Mock.class.getDeclaredField("field3")));

        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class, Mock.class.getDeclaredField("field4")));
        assertThrowsExactly(IllegalArgumentException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class, Mock.class.getDeclaredField("field5")));
    }

    @Test
    void testGetTargetModelClassFromSuper() throws NoSuchFieldException {
        assertEquals(TreeMock.class, RelationFieldUtil.getTargetModelClass(TreeMock.class, BaseTreeModel.class.getDeclaredField("parentId")));
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
        Field inverseField = RelationFieldUtil.getInverseField(Mock.class, Mock.class.getDeclaredField("field1"));
        assertEquals(Mock.class.getDeclaredField("field2"), inverseField);

        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getInverseField(Mock.class, Mock.class.getDeclaredField("field2")));
        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getInverseField(Mock.class, Mock.class.getDeclaredField("field3")));
    }

    @Getter
    @Setter
    static class Mock extends BaseModel {

        @One2ManyField(inverseField = "field2")
        private One2Many<Mock> field1;

        @Many2OneField
        private Many2One<Mock> field2;

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

    static class TreeMock extends BaseTreeModel<TreeMock> {
    }
}
