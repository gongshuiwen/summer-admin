package com.hzhg.plm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzhg.plm.core.entity.Mock1;
import com.hzhg.plm.core.entity.Mock2;
import com.hzhg.plm.core.fields.Command;
import com.hzhg.plm.core.fields.One2Many;
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
})
public class TestAbstractBaseServiceOne2Many {

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
    public void testOne2ManyCreateOneCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper);
        assertEquals(2, mock2s.size());
        assertEquals("mock2-1", mock2s.get(0).getName());
        assertEquals("mock2-2", mock2s.get(1).getName());
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE})
    public void testOne2ManyUpdateByIdCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        mock1Service.createOne(mock1);

        Mock1 mock1Update = new Mock1();
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1Update.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper);
        assertEquals(2, mock2s.size());
        assertEquals("mock2-1", mock2s.get(0).getName());
        assertEquals("mock2-2", mock2s.get(1).getName());
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_UPDATE,
            MOCK2_AUTHORITY_CREATE,
            MOCK2_AUTHORITY_SELECT,
            MOCK2_AUTHORITY_DELETE
    })
    public void testOne2ManyUpdateByIdCommandDelete() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Mock2::getMock1Id1, mock1.getId());
        List<Long> mock2Ids = mock2Mapper.selectList(wrapper1).stream().map(Mock2::getId).toList();
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock2s1(One2Many.ofCommands(List.of(Command.delete(mock2Ids))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper2);
        assertEquals(0, mock2s.size());
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_UPDATE,
            MOCK2_AUTHORITY_CREATE,
            MOCK2_AUTHORITY_SELECT,
            MOCK2_AUTHORITY_UPDATE
    })
    public void testOne2ManyUpdateByIdCommandUpdate() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Mock2::getMock1Id1, mock1.getId());
        List<Long> mock2Ids = mock2Mapper.selectList(wrapper1).stream().map(Mock2::getId).toList();
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock2s1(One2Many.ofCommands(List.of(Command.update(mock2Ids.get(0), new Mock2("mock2")))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper2);
        assertEquals(2, mock2s.size());
        assertEquals("mock2", mock2s.get(0).getName());
        assertEquals("mock2-2", mock2s.get(1).getName());
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_SELECT,
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_DELETE,
            MOCK2_AUTHORITY_CREATE,
            MOCK2_AUTHORITY_SELECT
    })
    public void testOne2ManyDeleteByIdRestrict() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        assertThrows(RuntimeException.class, () -> mock1Service.deleteById(mock1.getId()));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_SELECT,
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_DELETE,
            MOCK2_AUTHORITY_CREATE,
            MOCK2_AUTHORITY_SELECT,
            MOCK2_AUTHORITY_DELETE
    })
    public void testOne2ManyDeleteByIdCascade() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s2(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id2, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper);
        assertEquals(0, mock2s.size());
        assertNull(mock2Service.selectById(1L));
        assertNull(mock2Service.selectById(2L));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_SELECT,
            MOCK1_AUTHORITY_CREATE,
            MOCK1_AUTHORITY_DELETE,
            MOCK2_AUTHORITY_CREATE,
            MOCK2_AUTHORITY_SELECT
    })
    public void testOne2ManyDeleteByIdSetNull() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s3(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id3, mock1.getId());
        List<Mock2> mock2s = mock2Mapper.selectList(wrapper);
        assertEquals(0, mock2s.size());
        assertNull(mock2Service.selectById(1L).getMock1Id3().get());
        assertNull(mock2Service.selectById(2L).getMock1Id3().get());
    }
}
