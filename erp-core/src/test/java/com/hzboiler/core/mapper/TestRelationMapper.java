package com.hzboiler.core.mapper;

import com.hzboiler.core.model.Mock1;
import com.hzboiler.core.model.Mock3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {
        "/sql/test/ddl/mock1.sql",
        "/sql/test/ddl/mock2.sql",
        "/sql/test/ddl/mock_relation.sql",
        "/sql/test/data/mock1.sql",
        "/sql/test/data/mock2.sql",
        "/sql/test/data/mock_relation.sql",
})
class TestRelationMapper {

    @Autowired
    MockRelationMapper mapper;

    @Test
    void testGetTargetIds() {
        List<Long> mock2Ids = mapper.getTargetIds(Mock1.class, List.of(1L));
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
    }

    @Test
    void testRemoveAll() {
        mapper.removeAll(Mock1.class, 1L);
        assertEquals(0, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
    }

    @Test
    void testReplace() {
        mapper.replace(Mock1.class, 1L, List.of(1L));
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).size());
        assertEquals(1, mapper.getTargetIds(Mock1.class, List.of(1L)).get(0));
    }
}
