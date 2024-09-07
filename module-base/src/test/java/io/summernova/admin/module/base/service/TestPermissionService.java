package io.summernova.admin.module.base.service;

import io.summernova.admin.module.base.model.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/role.sql",
        "/sql/ddl/permission.sql",
        "/sql/ddl/role_permission.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/permission.sql",
        "/sql/test/data/role_permission.sql",
})
class TestPermissionService {

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserService userService;

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
