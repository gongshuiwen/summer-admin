package io.summernova.admin.module.base.service;

import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.test.context.BaseContextExtension;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestRoleService {

    RoleService roleService = new RoleServiceImpl();

    TestRoleService() throws NoSuchFieldException {
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/user.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/user_role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/user.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/user_role.sql");
    }

    @Test
    void testGetRolesByUserId() {
        assertEquals(Set.of(), getRoleIdsByUserId(0L));
        assertEquals(Set.of(1L, 2L), getRoleIdsByUserId(1L));
    }

    @Test
    void testAddUserRoles() {
        roleService.addUserRoles(1L, Set.of(1L, 2L));
        assertEquals(Set.of(1L, 2L), getRoleIdsByUserId(1L));

        roleService.addUserRoles(1L, Set.of(1L, 2L, 101L, 102L));
        assertEquals(Set.of(1L, 2L, 101L, 102L), getRoleIdsByUserId(1L));
    }

    @Test
    void testRemoveUserRoles() {
        roleService.removeUserRoles(1L, Set.of(1L));
        assertEquals(Set.of(2L), getRoleIdsByUserId(1L));

        roleService.removeUserRoles(1L, Set.of(1L, 2L));
        assertEquals(Set.of(), getRoleIdsByUserId(1L));
    }

    @Test
    void testReplaceUserRoles() {
        roleService.replaceUserRoles(1L, Set.of(1L, 2L, 101L, 102L));
        assertEquals(Set.of(1L, 2L, 101L, 102L), getRoleIdsByUserId(1L));

        roleService.replaceUserRoles(1L, Set.of());
        assertEquals(Set.of(), getRoleIdsByUserId(1L));
    }

    private Set<Long> getRoleIdsByUserId(Long userId) {
        return roleService.getRolesByUserId(userId).stream().map(Role::getId).collect(Collectors.toSet());
    }
}
