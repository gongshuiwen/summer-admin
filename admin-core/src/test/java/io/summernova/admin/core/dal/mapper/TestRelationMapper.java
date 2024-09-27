package io.summernova.admin.core.dal.mapper;

import io.summernova.admin.core.domain.model.Mock1;
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
    static final RelationMapper mapper;

    static {
        try {
            mapper = RelationMapperRegistry.getRelationMapper(
                    sqlSession, Mock1.class.getDeclaredField("mock3s"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(sqlSession, "mock1.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock3.sql");
        ScriptRunnerUtil.runScript(sqlSession, "mock_relation.sql");
    }

    @Test
    void testGetTargetIds() {
        List<Long> mock3Ids = mapper.getTargetIds(1L);
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));
    }

    @Test
    void testAdd() {
        // remove all
        mapper.removeAll(1L);
        assertEquals(0, mapper.getTargetIds(List.of(1L)).size());

        mapper.add(1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(List.of(1L)).get(0));

        mapper.add(1L, List.of(1L, 2L));
        assertEquals(2, mapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(List.of(1L)).get(0));
        assertEquals(2, mapper.getTargetIds(List.of(1L)).get(1));
    }

    @Test
    void testRemove() {
        mapper.remove(1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(List.of(1L)).size());
        assertEquals(2, mapper.getTargetIds(List.of(1L)).get(0));

        mapper.remove(1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(List.of(1L)).size());
        assertEquals(2, mapper.getTargetIds(List.of(1L)).get(0));
    }

    @Test
    void testRemoveAll() {
        mapper.removeAll(1L);
        assertEquals(0, mapper.getTargetIds(List.of(1L)).size());

        mapper.removeAll(1L);
        assertEquals(0, mapper.getTargetIds(List.of(1L)).size());
    }

    @Test
    void testReplace() {
        mapper.replace(1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(List.of(1L)).get(0));

        mapper.replace(1L, List.of());
        assertEquals(0, mapper.getTargetIds(List.of(1L)).size());
    }
}
