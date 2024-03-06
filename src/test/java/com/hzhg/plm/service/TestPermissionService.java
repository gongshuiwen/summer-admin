package com.hzhg.plm.service;

import com.hzhg.plm.entity.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/role.sql",
        "/sql/ddl/permission.sql",
        "/sql/ddl/role_permission.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/permission.sql",
        "/sql/test/data/role_permission.sql",
})
public class TestPermissionService {

    @Autowired
    PermissionService permissionService;

    @Test
    public void testGetPermissionsByRoleId() {
        Set<String> permissionsExpect = new HashSet<>(
                Arrays.asList("User:SELECT", "User:CREATE", "User:UPDATE", "User:DELETE"));
        Set<Permission> permissions = permissionService.getPermissionsByRoleId(1L);
        Assertions.assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    public void testGetPermissionsByRoleIds() {
        Set<String> permissionsExpect = new HashSet<>(
                Arrays.asList("User:SELECT", "User:CREATE", "User:UPDATE", "User:DELETE"));
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Collections.singleton(1L));
        Assertions.assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    public void testAddRolePermissionsByRoleId() {
        permissionService.addRolePermissions(2L, new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L)));
        Set<String> permissionsExpect = new HashSet<>(
                Arrays.asList("User:SELECT", "User:CREATE", "User:UPDATE", "User:DELETE"));
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Collections.singleton(2L));
        Assertions.assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    public void testRemoveRolePermissionsByRoleId() {
        permissionService.removeRolePermissions(1L, new HashSet<>(Arrays.asList(3L, 4L)));
        Set<String> permissionsExpect = new HashSet<>(
                Arrays.asList("User:SELECT", "User:CREATE"));
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Collections.singleton(1L));
        Assertions.assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }

    @Test
    public void testReplaceRolePermissionsByRoleId() {
        permissionService.replaceRolePermissions(1L, new HashSet<>(Arrays.asList(1L, 2L)));
        Set<String> permissionsExpect = new HashSet<>(
                Arrays.asList("User:SELECT", "User:CREATE"));
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(Collections.singleton(1L));
        Assertions.assertEquals(permissionsExpect, permissions.stream().map(Permission::getCode).collect(Collectors.toSet()));
    }
}
