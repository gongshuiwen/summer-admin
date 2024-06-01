package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.field.util.RelationFieldUtil;
import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRelationFieldUtil {

    @Getter
    @Setter
    public static class Mock extends BaseModel {
        private Many2One<Mock> field1;
        private One2Many<Mock> field2;
        private Many2Many<Mock> field3;
        private Many2One<?> field4;
        private Long field5;
    }

    @Test
    void testGetTargetModelClass() throws NoSuchFieldException {
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field1")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field2")));
        assertEquals(Mock.class, RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field3")));

        assertThrowsExactly(RuntimeException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field4")));
        assertThrowsExactly(IllegalArgumentException.class, () -> RelationFieldUtil.getTargetModelClass(Mock.class.getDeclaredField("field5")));
    }
}
