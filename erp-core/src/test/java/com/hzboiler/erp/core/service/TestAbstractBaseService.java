package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.model.Mock;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.OrderBys;
import com.hzboiler.erp.core.protocal.query.SimpleCondition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.hzboiler.erp.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@SpringBootTest
public class TestAbstractBaseService {

    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK_ENTITY_NAME;

    @Autowired
    MockService mockService;

    @AfterEach
    void afterEach() {
        BaseContextHolder.clearContext();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testSelectById() {
        Mock result = mockService.selectById(1L);
        assertEquals(1L, result.getId());
        assertEquals("mock1", result.getName());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testSelectByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<Mock> results = mockService.selectByIds(ids);
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("mock1", results.get(0).getName());
        assertEquals(2L, results.get(1).getId());
        assertEquals("mock2", results.get(1).getName());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testPage() {
        IPage<Mock> pageResult = mockService.page(1L, 20L);
        assertEquals(1, pageResult.getPages());
        assertEquals(2, pageResult.getTotal());
        assertEquals(1, pageResult.getCurrent());
        assertEquals(20, pageResult.getSize());

        List<Mock> results = pageResult.getRecords();
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("mock1", results.get(0).getName());
        assertEquals(2L, results.get(1).getId());
        assertEquals("mock2", results.get(1).getName());

        pageResult = mockService.page(1L, 20L, null, OrderBys.of("_id"));
        assertEquals(1, pageResult.getPages());
        assertEquals(2, pageResult.getTotal());
        assertEquals(1, pageResult.getCurrent());
        assertEquals(20, pageResult.getSize());

        results = pageResult.getRecords();
        assertEquals(2, results.size());
        assertEquals(2L, results.get(0).getId());
        assertEquals("mock2", results.get(0).getName());
        assertEquals(1L, results.get(1).getId());
        assertEquals("mock1", results.get(1).getName());

        Condition condition = SimpleCondition.of("id", "=", 1L);

        pageResult = mockService.page(1L, 20L, condition, OrderBys.of("_id"));
        assertEquals(1, pageResult.getPages());
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getCurrent());
        assertEquals(20, pageResult.getSize());

        results = pageResult.getRecords();
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("mock1", results.get(0).getName());

        pageResult = mockService.page(1L, 20L, condition);
        assertEquals(1, pageResult.getPages());
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getCurrent());
        assertEquals(20, pageResult.getSize());

        results = pageResult.getRecords();
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("mock1", results.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testCount() {
        Long count = mockService.count((Condition) null);
        assertEquals(2, count);

        Condition condition = SimpleCondition.of("id", "=", 1L);
        count = mockService.count(condition);
        assertEquals(1, count);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_CREATE})
    void testCreateOne() {
        Mock mock = new Mock("mock");
        mockService.createOne(mock);
        assertEquals(1, mockService.getBaseMapper().selectList(null).size());

        assertEquals(1L, mock.getId());
        assertEquals("mock", mock.getName());
        assertEquals(0, mock.getCreateUser());
        assertEquals(0, mock.getUpdateUser());
        assertNotNull(mock.getCreateTime());
        assertNotNull(mock.getUpdateTime());

        Mock result = mockService.getBaseMapper().selectById(mock.getId());
        assertEquals(mock.getId(), result.getId());
        assertEquals(mock.getName(), result.getName());
        assertEquals(mock.getCreateUser(), result.getCreateUser());
        assertEquals(mock.getUpdateUser(), result.getUpdateUser());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_CREATE})
    void testCreateBatch() {
        List<Mock> mocks = List.of(new Mock("mock1"), new Mock("mock2"));
        mockService.createBatch(mocks);
        assertEquals(2, mockService.getBaseMapper().selectList(null).size());

        for (int i = 0; i < mocks.size(); i++) {
            Mock mock = mocks.get(i);
            assertEquals(i + 1, mock.getId());
            assertEquals("mock" + (i + 1), mock.getName());
            assertEquals(0, mock.getCreateUser());
            assertEquals(0, mock.getUpdateUser());
            assertNotNull(mock.getCreateTime());
            assertNotNull(mock.getUpdateTime());

            Mock result = mockService.getBaseMapper().selectById(mock.getId());
            assertEquals(mock.getId(), result.getId());
            assertEquals(mock.getName(), result.getName());
            assertEquals(mock.getCreateUser(), result.getCreateUser());
            assertEquals(mock.getUpdateUser(), result.getUpdateUser());
            assertNotNull(result.getCreateTime());
            assertNotNull(result.getUpdateTime());
        }
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_UPDATE})
    void testUpdateById() {
        Long id = 1L;
        Mock mock = new Mock("mock");
        mockService.updateById(id, mock);

        Mock result = mockService.getBaseMapper().selectById(id);
        assertEquals(mock.getName(), result.getName());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_UPDATE})
    void testUpdateByIds() {
        List<Long> ids = List.of(1L, 2L);
        Mock mock = new Mock("mock");
        mockService.updateByIds(ids, mock);

        List<Mock> results = mockService.getBaseMapper().selectBatchIds(ids);
        for (Mock result : results) {
            assertEquals(mock.getName(), result.getName());
        }
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_DELETE})
    void testDeleteById() {
        Long id = 1L;
        assertNotNull(mockService.getBaseMapper().selectById(id));
        mockService.deleteById(id);
        assertNull(mockService.getBaseMapper().selectById(id));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = {MOCK_AUTHORITY_DELETE})
    void testDeleteByIds() {
        List<Long> ids = List.of(1L, 2L);
        assertEquals(2, mockService.getBaseMapper().selectBatchIds(ids).size());
        mockService.deleteByIds(ids);
        assertEquals(0, mockService.getBaseMapper().selectBatchIds(ids).size());
    }
}
