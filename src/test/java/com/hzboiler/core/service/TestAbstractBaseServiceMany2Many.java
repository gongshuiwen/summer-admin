package com.hzboiler.core.service;

import com.hzboiler.core.entity.Mock1;
import com.hzboiler.core.entity.Mock3;
import com.hzboiler.core.fields.Command;
import com.hzboiler.core.fields.Many2Many;
import com.hzboiler.core.mapper.Mock1Mapper;
import com.hzboiler.core.mapper.Mock3Mapper;
import com.hzboiler.core.mapper.MockRelationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.hzboiler.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@SpringBootTest
@Sql(scripts = {
        "/sql/test/ddl/mock1.sql",
        "/sql/test/ddl/mock3.sql",
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

    static final String MOCK3_ENTITY_NAME = "Mock3";
    static final String MOCK3_AUTHORITY_SELECT = MOCK3_ENTITY_NAME + ":" + AUTHORITY_SELECT;
    static final String MOCK3_AUTHORITY_CREATE = MOCK3_ENTITY_NAME + ":" + AUTHORITY_CREATE;

    @Autowired
    Mock1Mapper mock1Mapper;

    @Autowired
    Mock3Mapper mock3Mapper;

    @Autowired
    MockRelationMapper mockRelationMapper;

    @Autowired
    Mock1Service mock1Service;

    @Autowired
    Mock3Service mock3Service;

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_CREATE
    })
    public void testMany2ManyCreateOneCommandAdd() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.saveBatch(mock3List);

        // create mock1 with adding mock3s
        Mock1 mock1 = new Mock1("mock1-1");
        List<Command<Mock3>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1.setMock3s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        // check result
        List<Long> mock3Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_UPDATE,
            MOCK3_AUTHORITY_CREATE
    })
    public void testMany2ManyUpdateByIdCommandAdd() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.saveBatch(mock3List);

        // create mock1
        Mock1 mock1 = new Mock1("mock1-1");
        mock1Service.createOne(mock1);

        // update mock1 with adding mock3s
        Mock1 mock1Update = new Mock1();
        List<Command<Mock3>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1Update.setMock3s(Many2Many.ofCommands(commands));
        mock1Service.updateById(mock1.getId(), mock1Update);

        // check result
        List<Long> mock3Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(2, mock3Ids.size());
        assertEquals(1, mock3Ids.get(0));
        assertEquals(2, mock3Ids.get(1));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_UPDATE,
            MOCK3_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_SELECT
    })
    public void testMany2ManyUpdateByIdCommandRemove() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.saveBatch(mock3List);

        // create mock1 with adding mock3s
        Mock1 mock1 = new Mock1("mock1-1");
        List<Command<Mock3>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1.setMock3s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        // update mock1 with removing mock3s
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock3s(Many2Many.ofCommands(List.of(Command.remove(List.of(1L)))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        // check result
        List<Long> mock3Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(1, mock3Ids.size());
        assertEquals(2, mock3Ids.get(0));
        assertNotNull(mock3Service.selectById(1L));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_UPDATE,
            MOCK3_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_SELECT,
            MOCK3_AUTHORITY_SELECT
    })
    public void testMany2ManyUpdateByIdCommandReplace() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.saveBatch(mock3List);

        // create mock1 with adding mock3s
        Mock1 mock1 = new Mock1("mock1-1");
        List<Command<Mock3>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1.setMock3s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        // update mock1 with replace mock3s
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock3s(Many2Many.ofCommands(List.of(Command.replace(List.of(2L)))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        // check result
        List<Long> mock3Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(1, mock3Ids.size());
        assertEquals(2, mock3Ids.get(0));
        assertNotNull(mock3Service.selectById(1L));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_SELECT,
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_DELETE,
            MOCK2_AUTHORITY_SELECT,
            MOCK3_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_SELECT
    })
    public void testMany2ManyDeleteById() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.saveBatch(mock3List);

        // create mock1 with adding mock3s
        Mock1 mock1 = new Mock1("mock1-1");
        List<Command<Mock3>> commands = List.of(Command.add(List.of(1L, 2L)));
        mock1.setMock3s(Many2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        // delete mock1
        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        // check result
        List<Long> mock3Ids = mockRelationMapper.getTargetIds(mock1.getClass(), List.of(mock1.getId()));
        assertEquals(0, mock3Ids.size());
        assertNotNull(mock3Service.selectById(1L));
        assertNotNull(mock3Service.selectById(2L));
    }
}
