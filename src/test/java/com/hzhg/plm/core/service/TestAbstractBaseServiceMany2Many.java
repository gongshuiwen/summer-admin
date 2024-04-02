package com.hzhg.plm.core.service;

import com.hzhg.plm.core.entity.Mock1;
import com.hzhg.plm.core.entity.Mock2;
import com.hzhg.plm.core.fields.Command;
import com.hzhg.plm.core.fields.Many2Many;
import com.hzhg.plm.core.mapper.Mock1Mapper;
import com.hzhg.plm.core.mapper.Mock2Mapper;
import com.hzhg.plm.core.mapper.MockRelationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.hzhg.plm.core.security.DataAccessAuthority.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {
        "/sql/test/ddl/mock1.sql",
        "/sql/test/ddl/mock2.sql",
        "/sql/test/ddl/mock_relation.sql",
})
public class TestAbstractBaseServiceMany2Many {

    static final String MOCK1_ENTITY_NAME = "Mock1";
    static final String MOCK1_AUTHORITY_SELECT = MOCK1_ENTITY_NAME + ":" + AUTHORITY_SELECT;
    static final String MOCK1_AUTHORITY_CREATE = MOCK1_ENTITY_NAME + ":" + AUTHORITY_CREATE;
    static final String MOCK1_AUTHORITY_UPDATE = MOCK1_ENTITY_NAME + ":" + AUTHORITY_UPDATE;
    static final String MOCK1_AUTHORITY_DELETE = MOCK1_ENTITY_NAME + ":" + AUTHORITY_DELETE;

    static final String MOCK2_ENTITY_NAME = "Mock2";
    static final String MOCK2_AUTHORITY_SELECT = MOCK2_ENTITY_NAME + ":" + AUTHORITY_SELECT;
    static final String MOCK2_AUTHORITY_CREATE = MOCK2_ENTITY_NAME + ":" + AUTHORITY_CREATE;
    static final String MOCK2_AUTHORITY_UPDATE = MOCK2_ENTITY_NAME + ":" + AUTHORITY_UPDATE;
    static final String MOCK2_AUTHORITY_DELETE = MOCK2_ENTITY_NAME + ":" + AUTHORITY_DELETE;

    @Autowired
    Mock1Mapper mock1Mapper;

    @Autowired
    Mock2Mapper mock2Mapper;

    @Autowired
    MockRelationMapper mockRelationMapper;

    @Autowired
    Mock1Service mock1Service;

    @Autowired
    Mock2Service mock2Service;


    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK2_AUTHORITY_CREATE})
    public void testMany2ManyCreateOneCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(1L));
        assertEquals(2, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertEquals(2, mock2Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK2_AUTHORITY_CREATE})
    public void testMany2ManyCreateOneCommandAdd() {
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        mock2Service.saveBatch(mock2List);

        Mock1 mock1 = new Mock1("mock1-1");
        List<Command<Mock2>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(2, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertEquals(2, mock2Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE})
    public void testMany2ManyUpdateByIdCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        mock1Service.createOne(mock1);

        Mock1 mockUpdate = new Mock1();
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mockUpdate.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.updateById(mock1.getId(), mockUpdate);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(2, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertEquals(2, mock2Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE})
    public void testMany2ManyUpdateByIdCommandAdd() {
        Mock1 mock1 = new Mock1("mock1-1");
        mock1Service.createOne(mock1);

        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        mock2Service.saveBatch(mock2List);

        Mock1 mockUpdate = new Mock1();
        List<Command<Mock2>> commands = List.of(Command.add(List.of(1L, 2L)));
        mockUpdate.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.updateById(mock1.getId(), mockUpdate);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(2, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertEquals(2, mock2Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE, MOCK2_AUTHORITY_SELECT})
    public void testMany2ManyUpdateByIdCommandRemove() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        Mock1 mockUpdate = new Mock1();
        mockUpdate.setMock2s(Many2Many.ofCommands(List.of(Command.remove(List.of(1L)))));
        mock1Service.updateById(mock1.getId(), mockUpdate);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(1, mock2Ids.size());
        assertEquals(2, mock2Ids.get(0));
        assertNotNull(mock2Service.selectById(1L));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE, MOCK2_AUTHORITY_SELECT})
    public void testMany2ManyUpdateByIdCommandRemoveAll() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        Mock1 mockUpdate = new Mock1();
        mockUpdate.setMock2s(Many2Many.ofCommands(List.of(Command.removeAll())));
        mock1Service.updateById(mock1.getId(), mockUpdate);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(0, mock2Ids.size());
        assertNotNull(mock2Service.selectById(1L));
        assertNotNull(mock2Service.selectById(2L));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE, MOCK2_AUTHORITY_SELECT})
    public void testMany2ManyUpdateByIdCommandReplace() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        Mock1 mockUpdate = new Mock1();
        mockUpdate.setMock2s(Many2Many.ofCommands(List.of(Command.replace(List.of(1L)))));
        mock1Service.updateById(mock1.getId(), mockUpdate);

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(1, mock2Ids.size());
        assertEquals(1, mock2Ids.get(0));
        assertNotNull(mock2Service.selectById(2L));
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_SELECT, MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_DELETE, MOCK2_AUTHORITY_CREATE, MOCK2_AUTHORITY_SELECT})
    public void testMany2ManyDeleteById() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        List<Long> mock2Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(0, mock2Ids.size());
        assertNotNull(mock2Service.selectById(1L));
        assertNotNull(mock2Service.selectById(2L));
    }
}
