package com.hzboiler.module.base.service;

import com.hzboiler.module.base.model.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashSet;
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
                "User:SELECT",
                "User:UPDATE",
                "Department:SELECT",
                "Menu:SELECT",
                "Role:SELECT"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleId(2L);
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    void testGetPermissionsByRoleIds() {
        Set<String> permissionsExpect = Set.of(
                "User:SELECT",
                "User:UPDATE",
                "Department:SELECT",
                "Menu:SELECT",
                "Role:SELECT"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Set.of(2L));
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    void testAddRolePermissionsByRoleId() {
        permissionService.addRolePermissions(2L, Set.of(1L, 2L, 3L, 4L));
        Set<String> permissionsExpect = Set.of(
                "User:SELECT",
                "User:CREATE",
                "User:UPDATE",
                "User:DELETE",
                "Department:SELECT",
                "Menu:SELECT",
                "Role:SELECT"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Set.of(2L));
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    void testRemoveRolePermissionsByRoleId() {
        permissionService.removeRolePermissions(2L, new HashSet<>(Arrays.asList(2L, 3L, 4L)));
        Set<String> permissionsExpect = Set.of(
                "User:SELECT",
                "Department:SELECT",
                "Menu:SELECT",
                "Role:SELECT"
        );
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Set.of(2L));
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    void testReplaceRolePermissionsByRoleId() {
        permissionService.replaceRolePermissions(2L, Set.of(1L, 2L, 3L, 4L));
        Set<String> permissionsExpect = Set.of("User:SELECT", "User:CREATE", "User:UPDATE", "User:DELETE");
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Set.of(2L));
        assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }
}
