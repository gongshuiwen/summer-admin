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


@SpringBootTest
@AutoConfigureMockMvc
public class TestBaseController {

    static final String MOCK_PATH = "/mock";

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", Is.is("mock")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createUser", Is.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.updateUser", Is.is("1")))
        ;
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql"})
    public void testCreate() throws Exception {
        Mock mock = new Mock();
        mock.setName("mock");
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post(MOCK_PATH, mock)
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

        mock = mockService.getById(1);
        Assertions.assertEquals("mock", mock.getName());
        Assertions.assertEquals(0, mock.getCreateUser());
        Assertions.assertEquals(0, mock.getUpdateUser());
    }

    @Test
    @Sql(scripts = {"/sql/test/ddl/mock.sql", "/sql/test/data/mock.sql"})
    public void testUpdate() throws Exception {
        Mock mock = new Mock();
        mock.setName("mock1");
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
        Assertions.assertEquals("mock1", mock.getName());
        Assertions.assertEquals(1, mock.getCreateUser());
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
}
