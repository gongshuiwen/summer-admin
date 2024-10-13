package io.summernova.admin.core.dal.mapper;

import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.core.domain.model.Mock3;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestRelationMapper {

    static final SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    static final RelationMapper relationMapper;

    static {
        RelationMapperInfo relationMapperInfo = new RelationMapperInfo(
                Mock1.class, Mock3.class, "mock1_id", "mock3_id", "mock_relation");
        relationMapper = RelationMapperRegistry.getRelationMapper(sqlSession, relationMapperInfo);
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(sqlSession, "mock1.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock3.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock_relation.sql");
    }

    @Test
    void testGetTargetIds() {
        List<Long> mock3Ids = relationMapper.getTargetIds(1L);
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));
    }

    @Test
    void testAdd() {
        // remove all
        relationMapper.removeAll(1L);
        assertEquals(0, relationMapper.getTargetIds(List.of(1L)).size());

        relationMapper.add(1L, List.of(1L));
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).get(0));

        relationMapper.add(1L, List.of(1L, 2L));
        assertEquals(2, relationMapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).get(0));
        assertEquals(2, relationMapper.getTargetIds(List.of(1L)).get(1));
    }

    @Test
    void testRemove() {
        relationMapper.remove(1L, List.of(1L));
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).size());
        assertEquals(2, relationMapper.getTargetIds(List.of(1L)).get(0));

        relationMapper.remove(1L, List.of(1L));
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).size());
        assertEquals(2, relationMapper.getTargetIds(List.of(1L)).get(0));
    }

    @Test
    void testRemoveAll() {
        relationMapper.removeAll(1L);
        assertEquals(0, relationMapper.getTargetIds(List.of(1L)).size());

        relationMapper.removeAll(1L);
        assertEquals(0, relationMapper.getTargetIds(List.of(1L)).size());
    }

    @Test
    void testReplace() {
        relationMapper.replace(1L, List.of(1L));
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, relationMapper.getTargetIds(List.of(1L)).get(0));

        relationMapper.replace(1L, List.of());
        assertEquals(0, relationMapper.getTargetIds(List.of(1L)).size());
    }
}
