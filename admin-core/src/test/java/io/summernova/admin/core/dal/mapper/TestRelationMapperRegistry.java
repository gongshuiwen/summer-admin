package io.summernova.admin.core.dal.mapper;

import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.core.domain.model.Mock3;
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

        List<Long> mock3Ids = relationMapper.getTargetIds(1L);
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));

        RelationMapperInfo relationMapperInfo = relationMapper.getRelationMapperInfo();
        assertEquals(Mock1.class, relationMapperInfo.sourceClass());
        assertEquals(Mock3.class, relationMapperInfo.targetClass());
        assertEquals("mock1_id", relationMapperInfo.sourceField());
        assertEquals("mock3_id", relationMapperInfo.targetField());
        assertEquals("mock_relation", relationMapperInfo.joinTable());
    }

    @Test
    void testBuildRelationMapperInterface() {
        Class<?> mapperInterface = RelationMapperRegistry.buildRelationMapperInterface();

        // check mapper interface
        assertNotNull(mapperInterface);
        assertTrue(Modifier.isPublic(mapperInterface.getModifiers()));
        assertTrue(Modifier.isInterface(mapperInterface.getModifiers()));
        assertEquals(1, mapperInterface.getInterfaces().length);
        assertEquals(RelationMapper.class, mapperInterface.getInterfaces()[0]);
    }
}
