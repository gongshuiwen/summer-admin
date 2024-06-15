package com.hzboiler.erp.core.service;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.mapper.TreeMockMapper;
import com.hzboiler.erp.core.model.TreeMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@SpringBootTest
class TestAbstractBaseTreeService {

    @Autowired
    TreeMockMapper treeMockMapper;

    @Autowired
    TreeMockService treeMockService;

    @Test
    @Sql(scripts = {"/sql/test/ddl/tree_mock.sql", "/sql/test/data/tree_mock.sql"})
    @WithMockAdmin
    void testGetChildren() {
        TreeMock treeMock = treeMockMapper.selectById(1L);
        List<TreeMock> treeMocks = treeMockService.getChildren(treeMock);
        assertEquals(2, treeMocks.size());
        assertEquals(2L, treeMocks.get(0).getId());
        assertEquals(3L, treeMocks.get(1).getId());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/tree_mock.sql", "/sql/test/data/tree_mock.sql"})
    @WithMockAdmin
    void testGetAncestors() {
        TreeMock treeMock = treeMockMapper.selectById(4L);
        List<TreeMock> treeMocks = treeMockService.getAncestors(treeMock);
        assertEquals(2, treeMocks.size());
        assertEquals(1L, treeMocks.get(0).getId());
        assertEquals(2L, treeMocks.get(1).getId());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/tree_mock.sql", "/sql/test/data/tree_mock.sql"})
    @WithMockAdmin
    void testGetDescendants() {
        TreeMock treeMock = treeMockMapper.selectById(1L);
        List<TreeMock> treeMocks = treeMockService.getDescendants(treeMock);
        assertEquals(3, treeMocks.size());
        assertEquals(2L, treeMocks.get(0).getId());
        assertEquals(3L, treeMocks.get(1).getId());
        assertEquals(4L, treeMocks.get(2).getId());
    }
}
