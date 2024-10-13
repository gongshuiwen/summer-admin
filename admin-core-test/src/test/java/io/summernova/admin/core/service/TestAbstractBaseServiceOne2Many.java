package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.summernova.admin.test.annotation.WithMockUser;
import io.summernova.admin.test.context.BaseContextExtension;
import io.summernova.admin.core.domain.field.Command;
import io.summernova.admin.core.domain.field.One2Many;
import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.core.domain.model.Mock2;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.summernova.admin.core.security.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestAbstractBaseServiceOne2Many {

    static final String MOCK1_ENTITY_NAME = "Mock1";
    static final String MOCK1_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK1_ENTITY_NAME;
    static final String MOCK1_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK1_ENTITY_NAME;

    static final String MOCK2_ENTITY_NAME = "Mock2";
    static final String MOCK2_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK2_ENTITY_NAME;
    static final String MOCK2_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK2_ENTITY_NAME;
    static final String MOCK2_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK2_ENTITY_NAME;
    static final String MOCK2_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK2_ENTITY_NAME;

    Mock1Service mock1Service = new Mock1Service();
    Mock2Service mock2Service = new Mock2Service();
    Mock3Service mock3Service = new Mock3Service();

    TestAbstractBaseServiceOne2Many() {
        BaseServiceRegistry.register(mock1Service);
        BaseServiceRegistry.register(mock2Service);
        BaseServiceRegistry.register(mock3Service);
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock1.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "mock2.sql");
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK2_AUTHORITY_CREATE})
    void testOne2ManyCreateOneCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper);
        assertEquals(2, mock2s.size());
        assertEquals("mock2-1", mock2s.get(0).getName());
        assertEquals("mock2-2", mock2s.get(1).getName());
    }

    @Test
    @WithMockUser(authorities = {MOCK1_AUTHORITY_CREATE, MOCK1_AUTHORITY_UPDATE, MOCK2_AUTHORITY_CREATE})
    void testOne2ManyUpdateByIdCommandCreate() {
        Mock1 mock1 = new Mock1("mock1-1");
        mock1Service.createOne(mock1);

        Mock1 mock1Update = new Mock1();
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1Update.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper);
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
    void testOne2ManyUpdateByIdCommandDelete() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Mock2::getMock1Id1, mock1.getId());
        List<Long> mock2Ids = mock2Service.getBaseMapper().selectList(wrapper1).stream().map(Mock2::getId).toList();
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock2s1(One2Many.ofCommands(List.of(Command.delete(mock2Ids))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper2);
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
    void testOne2ManyUpdateByIdCommandUpdate() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s1(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        LambdaQueryWrapper<Mock2> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Mock2::getMock1Id1, mock1.getId());
        List<Long> mock2Ids = mock2Service.getBaseMapper().selectList(wrapper1).stream().map(Mock2::getId).toList();
        Mock1 mock1Update = new Mock1();
        mock1Update.setMock2s1(One2Many.ofCommands(List.of(Command.update(mock2Ids.get(0), new Mock2("mock2")))));
        mock1Service.updateById(mock1.getId(), mock1Update);

        LambdaQueryWrapper<Mock2> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Mock2::getMock1Id1, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper2);
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
    void testOne2ManyDeleteByIdRestrict() {
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
    void testOne2ManyDeleteByIdCascade() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s2(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<Mock2>()
                .eq(Mock2::getMock1Id2, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper);
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
    void testOne2ManyDeleteByIdSetNull() {
        Mock1 mock1 = new Mock1("mock1-1");
        List<Mock2> mock2List = List.of(new Mock2("mock2-1"), new Mock2("mock2-2"));
        List<Command<Mock2>> commands = List.of(Command.create(mock2List));
        mock1.setMock2s3(One2Many.ofCommands(commands));
        mock1Service.createOne(mock1);

        mock1Service.deleteById(mock1.getId());
        assertNull(mock1Service.selectById(mock1.getId()));

        LambdaQueryWrapper<Mock2> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock2::getMock1Id3, mock1.getId());
        List<Mock2> mock2s = mock2Service.getBaseMapper().selectList(wrapper);
        assertEquals(0, mock2s.size());
        assertNull(mock2Service.selectById(1L).getMock1Id3().getRecord());
        assertNull(mock2Service.selectById(2L).getMock1Id3().getRecord());
    }
}
