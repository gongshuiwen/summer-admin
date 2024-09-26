package io.summernova.admin.core.service;

import io.summernova.admin.core.annotaion.WithMockUser;
import io.summernova.admin.core.context.BaseContextExtension;
import io.summernova.admin.core.context.BaseContextHolder;
import io.summernova.admin.core.dal.mapper.RelationMapper;
import io.summernova.admin.core.dal.mapper.RelationMapperRegistry;
import io.summernova.admin.core.dal.mapper.ScriptRunnerUtil;
import io.summernova.admin.core.dal.mapper.SqlSessionUtil;
import io.summernova.admin.core.domain.field.Command;
import io.summernova.admin.core.domain.field.Many2Many;
import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.core.domain.model.Mock3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.summernova.admin.core.security.Constants.*;
import static io.summernova.admin.core.service.TestAbstractBaseServiceOne2Many.MOCK2_AUTHORITY_DELETE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestAbstractBaseServiceMany2Many {

    static final String MOCK1_ENTITY_NAME = "Mock1";
    static final String MOCK1_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK1_ENTITY_NAME;

    static final String MOCK2_ENTITY_NAME = "Mock2";
    static final String MOCK2_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK2_ENTITY_NAME;

    static final String MOCK3_ENTITY_NAME = "Mock3";
    static final String MOCK3_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK3_ENTITY_NAME;
    static final String MOCK3_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK3_ENTITY_NAME;

    RelationMapper mockRelationMapper;
    Mock1Service mock1Service = new Mock1Service();
    Mock2Service mock2Service = new Mock2Service();
    Mock3Service mock3Service = new Mock3Service();

    TestAbstractBaseServiceMany2Many() {
        BaseServiceRegistry.register(mock1Service);
        BaseServiceRegistry.register(mock2Service);
        BaseServiceRegistry.register(mock3Service);
    }

    @BeforeEach
    void beforeEach() throws NoSuchFieldException {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock1.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock2.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock3.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock_relation.sql");
        mockRelationMapper = RelationMapperRegistry.getRelationMapper(
                BaseContextHolder.getContext().getSqlSession(), Mock1.class.getDeclaredField("mock3s"));
    }

    @Test
    @WithMockUser(authorities = {
            MOCK1_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_CREATE
    })
    void testMany2ManyCreateOneCommandAdd() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.createBatch(mock3List);

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
    void testMany2ManyUpdateByIdCommandAdd() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.createBatch(mock3List);

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
    void testMany2ManyUpdateByIdCommandRemove() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.createBatch(mock3List);

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
    void testMany2ManyUpdateByIdCommandReplace() {
        // create mock3s
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.createBatch(mock3List);

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
            MOCK2_AUTHORITY_DELETE,
            MOCK3_AUTHORITY_CREATE,
            MOCK3_AUTHORITY_SELECT
    })
    void testMany2ManyDeleteById() {
        // create mock3 records
        List<Mock3> mock3List = List.of(new Mock3("mock3-1"), new Mock3("mock3-2"));
        mock3Service.createBatch(mock3List);

        // create mock1 with adding mock3 records
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
