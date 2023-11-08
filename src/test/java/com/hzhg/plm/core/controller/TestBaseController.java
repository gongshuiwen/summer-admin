package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.service.MockService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hzhg.plm.core.controller.BaseController.*;
import static com.hzhg.plm.core.test.ResultCheckUtils.checkResultActionsAccessDined;
import static com.hzhg.plm.core.test.ResultCheckUtils.checkResultActionsSuccess;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseController {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_GET = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_GET;
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_CREATE;
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_UPDATE;
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + AUTHORITY_DELIMITER + AUTHORITY_DELETE;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockService mockService;

    final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * get tests
     */
    ResultActions doGet(long getId) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get(MOCK_PATH + "/" + getId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_GET)
    void testGetAuthorized() throws Exception {
        long getId = 1;
        Mock mock = mockService.getById(getId);

        ResultActions resultActions = doGet(getId);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(getId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(mock.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("0")))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testGetNotAuthorized() throws Exception {
        ResultActions resultActions = doGet(1);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testGetAdmin() throws Exception {
        testGetAuthorized();
    }


    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testGetAnonymous() throws Exception {
        testGetNotAuthorized();
    }

    /**
     * create tests
     */
    ResultActions doCreate(String name) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post(MOCK_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Mock(name))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testCreateAuthorized() throws Exception {
        String name = "mock";
        long returnId = 1;
        Assertions.assertEquals(0, mockService.count());

        ResultActions resultActions = doCreate(name);

        checkResultActionsSuccess(resultActions);
        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(returnId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(name)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("0")))
        ;

        Assertions.assertEquals(1, mockService.count());

        Mock mock = mockService.getById(returnId);
        Assertions.assertEquals(name, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser
    void testCreateNotAuthorized() throws Exception {
        ResultActions resultActions = doCreate("mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testCreateAdmin() throws Exception {
        testCreateAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithAnonymousUser
    void testCreateAnonymous() throws Exception {
        testCreateNotAuthorized();
    }

    /**
     * update tests
     */
    ResultActions doUpdate(long updateId, String updateName) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put(MOCK_PATH + "/" + updateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Mock(updateName))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testUpdateAuthorized() throws Exception {
        long updateId = 1;
        String updateName = "mock";
        long count = mockService.count();
        Assertions.assertNotEquals(updateName, mockService.getById(updateId).getName());

        ResultActions resultActions = doUpdate(updateId, updateName);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockService.count());

        Mock mock = mockService.getById(updateId);
        Assertions.assertEquals(updateName, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testUpdateNotAuthorized() throws Exception {
        ResultActions resultActions = doUpdate(1, "mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testUpdateAdmin() throws Exception {
        testUpdateAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testUpdateAnonymous() throws Exception {
        testUpdateNotAuthorized();
    }

    /**
     * delete tests
     */
    ResultActions doDelete(long deleteId) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete(MOCK_PATH + "/" + deleteId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testDeleteAuthorized() throws Exception{
        long deleteId = 1;
        long count = mockService.count();
        Assertions.assertNotNull(mockService.getById(deleteId));

        ResultActions resultActions = doDelete(deleteId);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count - 1, mockService.count());
        Assertions.assertNull(mockService.getById(deleteId));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testDeleteNotAuthorized() throws Exception{
        ResultActions resultActions = doDelete(1);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testDeleteAdmin() throws Exception{
        testDeleteAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testDeleteAnonymous() throws Exception{
        testDeleteNotAuthorized();
    }

    /**
     * batchCreate tests
     */
    ResultActions doBatchCreate(List<Mock> mocks) throws Exception{
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post(MOCK_PATH_BATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(mocks)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_CREATE)
    void testBatchCreateAuthorized() throws Exception {
        Assertions.assertEquals(0, mockService.count());

        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));

        ResultActions resultActions = doBatchCreate(mocks);

        checkResultActionsSuccess(resultActions);
        resultActions
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is("1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is(mocks.get(0).getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].createUser", Is.is("0")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].updateUser", Is.is("0")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is("2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is(mocks.get(1).getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].createUser", Is.is("0")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].updateUser", Is.is("0")))
        ;

        Assertions.assertEquals(2, mockService.count());

        Mock mock1 = mockService.getById(1);
        Assertions.assertEquals(mocks.get(0).getName(), mock1.getName());
        Assertions.assertEquals(0, mock1.getCreateUser());
        Assertions.assertEquals(0, mock1.getUpdateUser());

        Mock mock2 = mockService.getById(2);
        Assertions.assertEquals(mocks.get(1).getName(), mock2.getName());
        Assertions.assertEquals(0, mock2.getCreateUser());
        Assertions.assertEquals(0, mock2.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser
    void testBatchCreateNotAuthorized() throws Exception {
        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));
        ResultActions resultActions = doBatchCreate(mocks);
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testBatchCreateAdmin() throws Exception {
        testBatchCreateAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithAnonymousUser
    void testBatchCreateAnonymous() throws Exception {
        testBatchCreateNotAuthorized();
    }

    /**
     * batchUpdate tests
     */
    ResultActions doBatchUpdate(List<Long> updateIds, String updateName) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put(MOCK_PATH_BATCH)
                .param("ids", updateIds.stream().map(Object::toString).collect(Collectors.joining(",")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new Mock(updateName))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_UPDATE)
    void testBatchUpdateAuthorized() throws Exception {
        long count = mockService.count();
        String updateName = "mock";
        List<Long> updateIds = Arrays.asList(1L, 2L);
        updateIds.forEach(id -> Assertions.assertNotEquals(updateName, mockService.getById(id).getName()));

        ResultActions resultActions = doBatchUpdate(updateIds, updateName);

        checkResultActionsSuccess(resultActions);
        Assertions.assertEquals(count, mockService.count());
        for (Long updateId : updateIds) {
            Mock mock = mockService.getById(updateId);
            Assertions.assertEquals(updateName, mock.getName());
            Assertions.assertEquals(0, mock.getCreateUser());
            Assertions.assertEquals(0, mock.getUpdateUser());
        }
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testBatchUpdateNotAuthorized() throws Exception {
        ResultActions resultActions = doBatchUpdate(Arrays.asList(1L, 2L), "mock");
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testBatchUpdateAdmin() throws Exception {
        testBatchUpdateAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testBatchUpdateAnonymous() throws Exception {
        testBatchUpdateNotAuthorized();
    }

    /**
     * batchDelete tests
     */
    ResultActions doBatchDelete(List<Long> deleteIds) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete(MOCK_PATH_BATCH)
                .param("ids", deleteIds.stream().map(Object::toString).collect(Collectors.joining(",")))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(authorities = MOCK_AUTHORITY_DELETE)
    void testBatchDeleteAuthorized() throws Exception{
        long count = mockService.count();
        List<Long> deleteIds = Arrays.asList(1L, 2L);
        deleteIds.forEach(id -> Assertions.assertNotNull(mockService.getById(id)));

        ResultActions resultActions = doBatchDelete(deleteIds);

        checkResultActionsSuccess(resultActions);
        deleteIds.forEach(deleteId -> Assertions.assertNull(mockService.getById(deleteId)));
        Assertions.assertEquals(count - deleteIds.size(), mockService.count());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser
    void testBatchDeleteNotAuthorized() throws Exception {
        ResultActions resultActions = doBatchDelete(Arrays.asList(1L, 2L));
        checkResultActionsAccessDined(resultActions);
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithMockUser(roles = ROLE_ADMIN)
    void testBatchDeleteAdmin() throws Exception {
        testBatchDeleteAuthorized();
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    @WithAnonymousUser
    void testBatchDeleteAnonymous() throws Exception{
        testBatchDeleteNotAuthorized();
    }
}
