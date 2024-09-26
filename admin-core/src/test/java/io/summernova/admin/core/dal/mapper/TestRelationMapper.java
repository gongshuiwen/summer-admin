package io.summernova.admin.core.dal.mapper;

import io.summernova.admin.core.model.Mock1;
import io.summernova.admin.core.model.Mock3;
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
        List<Long> mock2Ids = mapper.getTargetIds(Mock1.class, 1L);
        assertEquals(2, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertEquals(2, mock2Ids.get(1));

        List<Long> mock1Ids = mapper.getTargetIds(Mock3.class, List.of(1L));
        assertEquals(2, mock1Ids.size());
        assertEquals(1, mock1Ids.get(0));
        assertEquals(2, mock1Ids.get(1));
    }

    @Test
    void testAdd() {
        // remove all
        mapper.removeAll(Mock1.class, 1L);
        assertEquals(0, mapper.getTargetIds(Mock1.class, List.of(1L)).size());

        mapper.add(Mock1.class, 1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));

        mapper.add(Mock1.class, 1L, List.of(1L, 2L));
        assertEquals(2, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));
        assertEquals(2, mapper.getTargetIds(Mock1.class, List.of(1L)).get(1));
    }

    @Test
    void testRemove() {
        mapper.remove(Mock1.class, 1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(2, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));

        mapper.remove(Mock1.class, 1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(2, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));
    }

    @Test
    void testRemoveAll() {
        mapper.removeAll(Mock1.class, 1L);
        assertEquals(0, mapper.getTargetIds(Mock1.class, List.of(1L)).size());

        mapper.removeAll(Mock1.class, 1L);
        assertEquals(0, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
    }

    @Test
    void testReplace() {
        mapper.replace(Mock1.class, 1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));

        mapper.replace(Mock1.class, 1L, List.of());
        assertEquals(0, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
    }
}
