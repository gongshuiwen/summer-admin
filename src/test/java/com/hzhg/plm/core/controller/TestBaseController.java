package com.hzhg.plm.core.controller;

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


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseController {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_PATH_BATCH = MOCK_PATH + "/batch";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockService mockService;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testGet() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get(MOCK_PATH + "/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is("mock1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("0")))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    public void testCreate() throws Exception {

        Assertions.assertEquals(0, mockService.count());

        Mock mock = new Mock("mock");
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post(MOCK_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(mock)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is("mock")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("0")))
        ;

        Assertions.assertEquals(1, mockService.count());

        mock = mockService.getById(1);
        Assertions.assertEquals("mock", mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testUpdate() throws Exception {
        Mock mock = new Mock("mock");
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put(MOCK_PATH + "/1", mock)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(mock)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

        mock = mockService.getById(1);
        Assertions.assertEquals("mock", mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testDelete() throws Exception{
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .delete(MOCK_PATH + "/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Is.is(R.SUCCESS_CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(R.SUCCESS_MESSAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Is.is(true)))
        ;

        Assertions.assertNull(mockService.getById(1));
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
}
