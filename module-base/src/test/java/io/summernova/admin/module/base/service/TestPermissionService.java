package io.summernova.admin.module.base.service;

import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.test.annotation.SummerAdminTest;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
@SummerAdminTest
class TestPermissionService {

    PermissionService permissionService = new PermissionServiceImpl();

    TestPermissionService() throws NoSuchFieldException {
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/permission.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/role_permission.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/permission.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/role_permission.sql");
    }

    @Test
    void testGetPermissionsByRoleId() {
        Set<String> permissionsExpect = Set.of(
                "S:User",
                "U:User",
                "S:Department",
                "S:Menu",
                "S:Role"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleId(2L);
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    void testGetPermissionsByRoleIds() {
        Set<String> permissionsExpect = Set.of(
                "S:User",
                "U:User",
                "S:Department",
                "S:Menu",
                "S:Role"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Set.of(2L));
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }
}
