package com.hzboiler.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzboiler.core.mapper.MockMapper;
import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hzboiler.core.security.DataAccessAuthority.AUTHORITY_CREATE;
import static com.hzboiler.core.utils.ResultCheckUtil.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseControllerCreateBatch {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_CREATE;

    MockMvc mockMvc;

    MockMapper mockMapper;

    ObjectMapper objectMapper;

    public TestBaseControllerCreateBatch(
            @Autowired MockMvc mockMvc,
            @Autowired MockMapper mockMapper,
            @Autowired MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mockMvc = mockMvc;
        this.mockMapper = mockMapper;
        this.objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
    }

    ResultActions doCreateBatch(List<Mock> mocks) throws Exception {
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

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchEmptyList() throws Exception {
        checkResultActionsInvalidArguments(doCreateBatch(new ArrayList<>()));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchNameNull() throws Exception {
        checkResultActionsInvalidArguments(doCreateBatch(List.of(new Mock(null))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchNameEmpty() throws Exception {
        checkResultActionsInvalidArguments(doCreateBatch(List.of(new Mock(""))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchNameBlank() throws Exception {
        checkResultActionsInvalidArguments(doCreateBatch(List.of(new Mock("   "))));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchIdNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setId(1L);
        checkResultActionsInvalidArguments(doCreateBatch(List.of(mock)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchCreateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doCreateBatch(List.of(mock)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchUpdateTimeNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateTime(LocalDateTime.now());
        checkResultActionsInvalidArguments(doCreateBatch(List.of(mock)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchCreateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setCreateUser(1L);
        checkResultActionsInvalidArguments(doCreateBatch(List.of(mock)));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    @WithMockAdmin
    void testCreateBatchUpdateUserNotNull() throws Exception {
        Mock mock = new Mock("mock");
        mock.setUpdateUser(1L);
        checkResultActionsInvalidArguments(doCreateBatch(List.of(mock)));
    }
}
