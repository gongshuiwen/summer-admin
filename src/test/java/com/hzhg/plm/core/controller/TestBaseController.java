package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.mapper.MockMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzhg.plm.core.controller.BaseController.*;
import static com.hzhg.plm.core.utils.ResultCheckUtils.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseController {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_SELECT;
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_CREATE;
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_UPDATE;
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_DELETE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseController (
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(roles = ROLE_ADMIN)
    public @interface WithMockAdmin {}

    /**
     * selectById tests
     */
    ResultActions doSelectById(long getId) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get(MOCK_PATH + "/" + getId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testSelectByIdAuthorized() throws Exception {
        long getId = 1;
        Mock mock = mockMapper.selectById(getId);

        ResultActions resultActions = doSelectById(getId);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(getId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(mock.getName())))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testSelectByIdNotAuthorized() throws Exception {
        ResultActions resultActions = doSelectById(1);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testSelectByIdAdmin() throws Exception {
        testSelectByIdAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testSelectByIdAnonymous() throws Exception {
        ResultActions resultActions = doSelectById(1);
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * selectByIds tests
     */
    ResultActions doSelectByIds(List<Long> ids) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(MOCK_PATH_BATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_SELECT)
    void testSelectByIdsAuthorized() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<Mock> mocks = mockMapper.selectBatchIds(ids);

        ResultActions resultActions = doSelectByIds(ids);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is(mocks.get(0).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(mocks.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is(mocks.get(1).getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(mocks.get(1).getName())))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testSelectByIdsNotAuthorized() throws Exception {
        ResultActions resultActions = doSelectByIds(List.of(1L, 2L));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testSelectByIdsAdmin() throws Exception {
        testSelectByIdsAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testSelectByIdsAnonymous() throws Exception {
        ResultActions resultActions = doSelectByIds(List.of(1L, 2L));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * createOne tests
     */
    ResultActions doCreateOne(String name) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post(MOCK_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Mock(name))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testCreateOneAuthorized() throws Exception {
        String name = "mock";
        long returnId = 1;
        Assertions.assertEquals(0, mockMapper.selectCount(null));

        ResultActions resultActions = doCreateOne(name);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(returnId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(name)))
        ;

        Assertions.assertEquals(1, mockMapper.selectCount(null));

        Mock mock = mockMapper.selectById(returnId);
        Assertions.assertEquals(name, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser
    void testCreateOneNotAuthorized() throws Exception {
        ResultActions resultActions = doCreateOne("mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateOneAdmin() throws Exception {
        testCreateOneAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithAnonymousUser
    void testCreateOneAnonymous() throws Exception {
        ResultActions resultActions = doCreateOne("mock");
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * createBatch tests
     */
    ResultActions doCreateBatch(List<Mock> mocks) throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(MOCK_PATH_BATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(mocks)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testCreateBatchAuthorized() throws Exception {
        Assertions.assertEquals(0, mockMapper.selectCount(null));

        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));

        ResultActions resultActions = doCreateBatch(mocks);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(mocks.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(mocks.get(1).getName())))
        ;

        Assertions.assertEquals(2, mockMapper.selectCount(null));

        Mock mock1 = mockMapper.selectById(1);
        Assertions.assertEquals(mocks.get(0).getName(), mock1.getName());
        Assertions.assertEquals(0, mock1.getCreateUser());
        Assertions.assertEquals(0, mock1.getUpdateUser());

        Mock mock2 = mockMapper.selectById(2);
        Assertions.assertEquals(mocks.get(1).getName(), mock2.getName());
        Assertions.assertEquals(0, mock2.getCreateUser());
        Assertions.assertEquals(0, mock2.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser
    void testCreateBatchNotAuthorized() throws Exception {
        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));
        ResultActions resultActions = doCreateBatch(mocks);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchAdmin() throws Exception {
        testCreateBatchAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithAnonymousUser
    void testCreateBatchAnonymous() throws Exception {
        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));
        ResultActions resultActions = doCreateBatch(mocks);
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * updateById tests
     */
    ResultActions doUpdateById(long updateId, String updateName) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put(MOCK_PATH + "/" + updateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Mock(updateName))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testUpdateByIdAuthorized() throws Exception {
        long updateId = 1;
        String updateName = "mock";
        long count = mockMapper.selectCount(null);
        Assertions.assertNotEquals(updateName, mockMapper.selectById(updateId).getName());

        ResultActions resultActions = doUpdateById(updateId, updateName);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockMapper.selectCount(null));

        Mock mock = mockMapper.selectById(updateId);
        Assertions.assertEquals(updateName, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testUpdateByIdNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdateById(1, "mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdAdmin() throws Exception {
        testUpdateByIdAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testUpdateByIdAnonymous() throws Exception {
        ResultActions resultActions = doUpdateById(1, "mock");
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * updateByIds tests
     */
    ResultActions doUpdateByIds(List<Long> updateIds, Mock mock) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(MOCK_PATH_BATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BatchUpdateDto<>(updateIds, mock))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testUpdateByIdsAuthorized() throws Exception {
        long count = mockMapper.selectCount(null);
        String updateName = "mock";
        List<Long> updateIds = Arrays.asList(1L, 2L);
        updateIds.forEach(id -> Assertions.assertNotEquals(updateName, mockMapper.selectById(id).getName()));

        ResultActions resultActions = doUpdateByIds(updateIds, new Mock(updateName));

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockMapper.selectCount(null));
        for (Long updateId : updateIds) {
            Mock mock = mockMapper.selectById(updateId);
            Assertions.assertEquals(updateName, mock.getName());
            Assertions.assertEquals(0, mock.getCreateUser());
            Assertions.assertEquals(0, mock.getUpdateUser());
        }
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testUpdateByIdsNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L), new Mock("mock"));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsAdmin() throws Exception {
        testUpdateByIdsAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testUpdateByIdsAnonymous() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L),  new Mock("mock"));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments1() throws Exception {
        ResultActions resultActions = doUpdateByIds(null, new Mock(""));
        checkResultActionsInvalidArguments(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments2() throws Exception {
        ResultActions resultActions = doUpdateByIds(new ArrayList<>(), new Mock(""));
        checkResultActionsInvalidArguments(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testUpdateByIdsInvalidArguments3() throws Exception {
        ResultActions resultActions = doUpdateByIds(Arrays.asList(1L, 2L), null);
        checkResultActionsInvalidArguments(resultActions);
    }

    /**
     * deleteById tests
     */
    ResultActions doDeleteById(long deleteId) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete(MOCK_PATH + "/" + deleteId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testDeleteByIdAuthorized() throws Exception{
        long deleteId = 1;
        long count = mockMapper.selectCount(null);
        Assertions.assertNotNull(mockMapper.selectById(deleteId));

        ResultActions resultActions = doDeleteById(deleteId);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count - 1, mockMapper.selectCount(null));
        Assertions.assertNull(mockMapper.selectById(deleteId));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testDeleteByIdNotAuthorized() throws Exception{
        ResultActions resultActions = doDeleteById(1);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testDeleteByIdAdmin() throws Exception{
        testDeleteByIdAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testDeleteByIdAnonymous() throws Exception{
        ResultActions resultActions = doDeleteById(1);
        checkResultActionsAuthenticationFailed(resultActions);
    }

    /**
     * deleteByIds tests
     */
    ResultActions doDeleteByIds(List<Long> deleteIds) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete(MOCK_PATH_BATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new BatchDeleteDto(deleteIds))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testDeleteByIdsAuthorized() throws Exception{
        long count = mockMapper.selectCount(null);
        List<Long> deleteIds = Arrays.asList(1L, 2L);
        deleteIds.forEach(id -> Assertions.assertNotNull(mockMapper.selectById(id)));

        ResultActions resultActions = doDeleteByIds(deleteIds);

        checkResultActionsSuccess(resultActions);
        deleteIds.forEach(deleteId -> Assertions.assertNull(mockMapper.selectById(deleteId)));
        Assertions.assertEquals(count - deleteIds.size(), mockMapper.selectCount(null));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testDeleteByIdsNotAuthorized() throws Exception {
        ResultActions resultActions = doDeleteByIds(Arrays.asList(1L, 2L));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testDeleteByIdsAdmin() throws Exception {
        testDeleteByIdsAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testDeleteByIdsAnonymous() throws Exception{
        ResultActions resultActions = doDeleteByIds(Arrays.asList(1L, 2L));
        checkResultActionsAuthenticationFailed(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testDeleteByIdsInvalidArguments1() throws Exception {
        ResultActions resultActions = doDeleteByIds(null);
        checkResultActionsInvalidArguments(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockAdmin
    void testDeleteByIdsInvalidArguments2() throws Exception {
        ResultActions resultActions = doDeleteByIds(new ArrayList<>());
        checkResultActionsInvalidArguments(resultActions);
    }
}
