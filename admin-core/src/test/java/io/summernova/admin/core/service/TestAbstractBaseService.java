package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.summernova.admin.common.query.CompareCondition;
import io.summernova.admin.common.query.Condition;
import io.summernova.admin.common.query.OrderBys;
import io.summernova.admin.core.annotaion.WithMockUser;
import io.summernova.admin.core.context.BaseContextExtension;
import io.summernova.admin.core.dal.mapper.ScriptRunnerUtil;
import io.summernova.admin.core.dal.mapper.SqlSessionUtil;
import io.summernova.admin.core.model.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.summernova.admin.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
public class TestAbstractBaseService {

    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK_ENTITY_NAME;

    MockService mockService = new MockService();

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock.sql");
    }

    @Test
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testSelectById() {
        Mock result = mockService.selectById(1L);
        assertEquals(1L, result.getId());
        assertEquals("mock1", result.getName());
    }

    @Test
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

        Condition condition = CompareCondition.eq("id", 1L);

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
    @WithMockUser(authorities = {MOCK_AUTHORITY_SELECT})
    void testCount() {
        Long count = mockService.count((Condition) null);
        assertEquals(2, count);

        Condition condition = CompareCondition.eq("id", 1L);
        count = mockService.count(condition);
        assertEquals(1, count);
    }

    @Test
    @WithMockUser(authorities = {MOCK_AUTHORITY_CREATE, MOCK_AUTHORITY_DELETE})
    void testCreateOne() {
        Mock mock = new Mock("mock");
        mockService.createOne(mock);
        assertEquals(3, mockService.getBaseMapper().selectList(null).size());

        assertEquals(3L, mock.getId());
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
    @WithMockUser(authorities = {MOCK_AUTHORITY_CREATE, MOCK_AUTHORITY_DELETE})
    void testCreateBatch() {
        List<Mock> mocks = List.of(new Mock("mock1"), new Mock("mock2"));
        mockService.createBatch(mocks);
        assertEquals(4, mockService.getBaseMapper().selectList(null).size());

        for (int i = 0; i < mocks.size(); i++) {
            Mock mock = mocks.get(i);
            assertEquals(i + 3, mock.getId());
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
    @WithMockUser(authorities = {MOCK_AUTHORITY_UPDATE})
    void testUpdateById() {
        Long id = 1L;
        Mock mock = new Mock("mock");
        mockService.updateById(id, mock);

        Mock result = mockService.getBaseMapper().selectById(id);
        assertEquals(mock.getName(), result.getName());
    }

    @Test
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
    @WithMockUser(authorities = {MOCK_AUTHORITY_DELETE})
    void testDeleteById() {
        Long id = 1L;
        assertNotNull(mockService.getBaseMapper().selectById(id));
        mockService.deleteById(id);
        assertNull(mockService.getBaseMapper().selectById(id));
    }

    @Test
    @WithMockUser(authorities = {MOCK_AUTHORITY_DELETE})
    void testDeleteByIds() {
        List<Long> ids = List.of(1L, 2L);
        assertEquals(2, mockService.getBaseMapper().selectBatchIds(ids).size());
        mockService.deleteByIds(ids);
        assertEquals(0, mockService.getBaseMapper().selectBatchIds(ids).size());
    }
}
