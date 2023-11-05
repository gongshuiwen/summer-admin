package com.hzhg.plm.core.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.protocal.R;
import com.hzhg.plm.core.service.MockService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseController {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";

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

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testGet() throws Exception {

        long getId = 1;
        String getName = "mock1";

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get(MOCK_PATH + "/" + getId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is(Long.toString(getId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is(getName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("0")))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    public void testCreate() throws Exception {

        String name = "mock";
        long returnId = 1;
        Assertions.assertEquals(0, mockService.count());

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post(MOCK_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new Mock(name))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
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
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testUpdate() throws Exception {

        long updateId = 1;
        String updateName = "mock";
        long count = mockService.count();
        Assertions.assertNotEquals(updateName, mockService.getById(updateId).getName());

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(MOCK_PATH + "/" + updateId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new Mock(updateName))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

        Assertions.assertEquals(count, mockService.count());

        Mock mock = mockService.getById(updateId);
        Assertions.assertEquals(updateName, mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testDelete() throws Exception{

        long deleteId = 1;
        long count = mockService.count();
        Assertions.assertNotNull(mockService.getById(deleteId));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(MOCK_PATH + "/" + deleteId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

        Assertions.assertEquals(count - 1, mockService.count());
        Assertions.assertNull(mockService.getById(deleteId));
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    public void testBatchCreate() throws Exception {

        Assertions.assertEquals(0, mockService.count());

        ArrayList<Mock> mocks = new ArrayList<>();
        mocks.add(new Mock("mock1"));
        mocks.add(new Mock("mock2"));
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post(MOCK_PATH_BATCH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(mocks)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Is.is("mock1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].updateUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Is.is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", Is.is("mock2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].updateUser", Is.is("0")))
        ;

        Assertions.assertEquals(2, mockService.count());

        Mock mock1 = mockService.getById(1);
        Assertions.assertEquals("mock1", mock1.getName());
        Assertions.assertEquals(0, mock1.getCreateUser());
        Assertions.assertEquals(0, mock1.getUpdateUser());

        Mock mock2 = mockService.getById(2);
        Assertions.assertEquals("mock2", mock2.getName());
        Assertions.assertEquals(0, mock2.getCreateUser());
        Assertions.assertEquals(0, mock2.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testBatchUpdate() throws Exception {

        List<Long> updateIds = Arrays.asList(1L, 2L);
        String updateName = "mock";
        long count = mockService.count();
        mockService.list().forEach(mock -> Assertions.assertNotEquals(updateName, mock.getName()));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(MOCK_PATH_BATCH)
                                .param("ids", updateIds.stream().map(Object::toString).collect(Collectors.joining(",")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new Mock("mock"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

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
    public void testBatchDelete() throws Exception{

        List<Long> deleteIds = Arrays.asList(1L, 2L);
        long count = mockService.count();
        deleteIds.forEach(deleteId -> Assertions.assertNotNull(mockService.getById(deleteId)));

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(MOCK_PATH_BATCH)
                                .param("ids", deleteIds.stream().map(Object::toString).collect(Collectors.joining(",")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

        Assertions.assertEquals(count - 2, mockService.count());
        deleteIds.forEach(deleteId -> Assertions.assertNull(mockService.getById(deleteId)));
    }
}
