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
        RelationMapperInfo relationMapperInfo = new RelationMapperInfo(
                Mock1.class, Mock3.class, "mock1_id", "mock3_id", "mock_relation");
        RelationMapper relationMapper = RelationMapperRegistry.getRelationMapper(sqlSession, relationMapperInfo);

        List<Long> mock3Ids = relationMapper.getTargetIds(1L);
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));

        assertEquals(relationMapperInfo, relationMapper.getRelationMapperInfo());
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
