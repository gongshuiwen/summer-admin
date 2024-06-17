package com.hzboiler.erp.core.service;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.field.Many2One;
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
@Sql(scripts = {"/sql/test/ddl/tree_mock.sql", "/sql/test/data/tree_mock.sql"})
class TestAbstractBaseTreeService {

    @Autowired
    TreeMockMapper treeMockMapper;

    @Autowired
    TreeMockService treeMockService;

    @Test
    @WithMockAdmin
    void testGetChildren() {
        TreeMock treeMock = treeMockMapper.selectById(1L);
        List<TreeMock> treeMocks = treeMockService.getChildren(treeMock);
        assertEquals(2, treeMocks.size());
        assertEquals(2L, treeMocks.get(0).getId());
        assertEquals(3L, treeMocks.get(1).getId());
    }

    @Test
    @WithMockAdmin
    void testGetAncestors() {
        TreeMock treeMock = treeMockMapper.selectById(4L);
        List<TreeMock> treeMocks = treeMockService.getAncestors(treeMock);
        assertEquals(2, treeMocks.size());
        assertEquals(1L, treeMocks.get(0).getId());
        assertEquals(2L, treeMocks.get(1).getId());
    }

    @Test
    @WithMockAdmin
    void testGetDescendants() {
        TreeMock treeMock = treeMockMapper.selectById(1L);
        List<TreeMock> treeMocks = treeMockService.getDescendants(treeMock);
        assertEquals(3, treeMocks.size());
        assertEquals(2L, treeMocks.get(0).getId());
        assertEquals(3L, treeMocks.get(1).getId());
        assertEquals(4L, treeMocks.get(2).getId());
    }

    @Test
    @WithMockAdmin
    void testCreate1() {
        TreeMock treeMock = new TreeMock();
        treeMock.setParentId(Many2One.ofId(1L));
        treeMockService.createOne(treeMock);

        assertEquals(treeMock.getParentPath(), "1");

        List<TreeMock> treeMocks = treeMockService.getAncestors(treeMock);
        assertEquals(1, treeMocks.size());
        assertEquals(1L, treeMocks.get(0).getId());

        List<TreeMock> descendants = treeMockService.getDescendants(treeMockMapper.selectById(1L));
        assertEquals(4, descendants.size());
        assertEquals(2L, descendants.get(0).getId());
        assertEquals(3L, descendants.get(1).getId());
        assertEquals(5L, descendants.get(2).getId());
        assertEquals(4L, descendants.get(3).getId());

        List<TreeMock> children = treeMockService.getChildren(treeMockMapper.selectById(1L));
        assertEquals(3, children.size());
        assertEquals(2L, children.get(0).getId());
        assertEquals(3L, children.get(1).getId());
        assertEquals(5L, children.get(2).getId());
    }

    @Test
    @WithMockAdmin
    void testCreate2() {
        TreeMock treeMock = new TreeMock();
        treeMock.setParentId(Many2One.ofId(4L));
        treeMockService.createOne(treeMock);

        assertEquals(treeMock.getParentPath(), "1/2/4");

        List<TreeMock> treeMocks = treeMockService.getAncestors(treeMock);
        assertEquals(3, treeMocks.size());
        assertEquals(1L, treeMocks.get(0).getId());
        assertEquals(2L, treeMocks.get(1).getId());
        assertEquals(4L, treeMocks.get(2).getId());

        List<TreeMock> descendants = treeMockService.getDescendants(treeMockMapper.selectById(1L));
        assertEquals(4, descendants.size());
        assertEquals(2L, descendants.get(0).getId());
        assertEquals(3L, descendants.get(1).getId());
        assertEquals(4L, descendants.get(2).getId());
        assertEquals(5L, descendants.get(3).getId());

        List<TreeMock> children = treeMockService.getChildren(treeMockMapper.selectById(4L));
        assertEquals(1, children.size());
        assertEquals(5L, children.get(0).getId());
    }

    @Test
    @WithMockAdmin
    void testUpdate1() {
        assertEquals("", treeMockMapper.selectById(1L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(2L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(3L).getParentPath());
        assertEquals("1/2", treeMockMapper.selectById(4L).getParentPath());

        TreeMock updateValues = new TreeMock();
        updateValues.setParentId(Many2One.ofId(0L));
        treeMockService.updateById(2L, updateValues);

        assertEquals("", treeMockMapper.selectById(1L).getParentPath());
        assertEquals("", treeMockMapper.selectById(2L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(3L).getParentPath());
        assertEquals("2", treeMockMapper.selectById(4L).getParentPath());
    }

    @Test
    @WithMockAdmin
    void testUpdate2() {
        assertEquals("", treeMockMapper.selectById(1L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(2L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(3L).getParentPath());
        assertEquals("1/2", treeMockMapper.selectById(4L).getParentPath());

        TreeMock updateValues = new TreeMock();
        updateValues.setParentId(Many2One.ofId(3L));
        treeMockService.updateById(2L, updateValues);

        assertEquals("", treeMockMapper.selectById(1L).getParentPath());
        assertEquals("1/3", treeMockMapper.selectById(2L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(3L).getParentPath());
        assertEquals("1/3/2", treeMockMapper.selectById(4L).getParentPath());
    }

    @Test
    @WithMockAdmin
    void testUpdate3() {
        assertEquals("", treeMockMapper.selectById(1L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(2L).getParentPath());
        assertEquals("1", treeMockMapper.selectById(3L).getParentPath());
        assertEquals("1/2", treeMockMapper.selectById(4L).getParentPath());

        TreeMock updateValues = new TreeMock();
        updateValues.setParentId(Many2One.ofId(4L));
        assertThrows(IllegalArgumentException.class, () -> treeMockService.updateById(2L, updateValues));
    }

    @Test
    @WithMockAdmin
    void testDelete() {
        treeMockService.deleteById(1L);
        assertNull(treeMockMapper.selectById(1L));
        assertNull(treeMockMapper.selectById(2L));
        assertNull(treeMockMapper.selectById(3L));
        assertNull(treeMockMapper.selectById(4L));
    }

    @Test
    @WithMockAdmin
    void testBuildTree() {
        List<TreeMock> treeMocks = treeMockService.lambdaQuery().list();
        List<TreeMock> tree = treeMockService.buildTree(treeMocks);

        assertEquals(1, tree.size());
        assertEquals(1L, tree.get(0).getId());

        TreeMock root = tree.get(0);
        List<TreeMock> children = root.getChildren().getRecords();
        assertEquals(2, children.size());
        assertEquals(2L, children.get(0).getId());
        assertEquals(3L, children.get(1).getId());

        TreeMock child = children.get(0);
        List<TreeMock> grandChildren = child.getChildren().getRecords();
        assertEquals(1, grandChildren.size());
        assertEquals(4L, grandChildren.get(0).getId());
    }
}

