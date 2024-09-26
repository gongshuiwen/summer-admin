package io.summernova.admin.core.mapper;

import io.summernova.admin.core.model.Mock1;
import io.summernova.admin.core.model.Mock3;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author gongshuiwen
 */
class TestRelationMapperRegistry {

    static final SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(sqlSession, "mock1.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock3.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock_relation.sql");
    }

    @Test
    void testGetRelationMapper() throws NoSuchFieldException {
        RelationMapper relationMapper = RelationMapperRegistry.getRelationMapper(
                sqlSession, Mock1.class.getDeclaredField("mock3s"));

        List<Long> mock3Ids = relationMapper.getTargetIds(Mock1.class, 1L);
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));
    }

    @Test
    void testBuildMapperInterface() throws ClassNotFoundException {
        Class<?> sourceClass = Mock1.class;
        Class<?> targetClass = Mock3.class;
        String sourField = "mock1_id";
        String targetField = "mock3_id";
        String joinTable = "mock_relation";
        Class<?> mapperInterface;
        mapperInterface = RelationMapperRegistry.buildMapperInterface(sourceClass, targetClass, sourField, targetField, joinTable);

        // check mapper interface
        assertNotNull(mapperInterface);
        assertTrue(Modifier.isPublic(mapperInterface.getModifiers()));
        assertTrue(Modifier.isInterface(mapperInterface.getModifiers()));
        assertEquals(1, mapperInterface.getInterfaces().length);
        assertEquals(RelationMapper.class, mapperInterface.getInterfaces()[0]);
        assertEquals(1, mapperInterface.getAnnotations().length);
        assertEquals(RelationMapperInfo.class, mapperInterface.getAnnotations()[0].annotationType());

        // check @MapperRelation annotation
        RelationMapperInfo relationMapperInfo = mapperInterface.getAnnotation(RelationMapperInfo.class);
        assertEquals("mock_relation", relationMapperInfo.table());
        assertEquals("mock1_id", relationMapperInfo.field1());
        assertEquals(Mock1.class, relationMapperInfo.class1());
        assertEquals("mock3_id", relationMapperInfo.field2());
        assertEquals(Mock3.class, relationMapperInfo.class2());
    }
}
