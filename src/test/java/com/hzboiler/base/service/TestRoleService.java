package com.hzboiler.base.service;

import com.hzboiler.module.base.model.Role;
import com.hzboiler.module.base.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/role.sql",
        "/sql/ddl/user.sql",
        "/sql/ddl/user_role.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/user.sql",
        "/sql/test/data/user_role.sql",
})
public class TestRoleService {

    @Autowired
    RoleService roleService;

    @Test
    public void testGetRolesByUserId() {
        assertEquals(Set.of(), roleService.getRolesByUserId(0L).stream().map(Role::getId).collect(Collectors.toSet()));
        assertEquals(Set.of(1L, 2L), roleService.getRolesByUserId(1L).stream().map(Role::getId).collect(Collectors.toSet()));
    }

    @Test
    public void testAddUserRoles() {
        roleService.addUserRoles(1L, new HashSet<>(List.of(1L, 2L, 101L, 102L)));
        Set<Role> roles = roleService.getRolesByUserId(1L);
        assertEquals(Set.of(1L, 2L, 101L, 102L), roles.stream().map(Role::getId).collect(Collectors.toSet()));
    }

    @Test
    public void testRemoveUserRoles() {
        roleService.removeUserRoles(1L, Set.of(1L, 2L, 101L, 102L));
        Set<Role> roles = roleService.getRolesByUserId(1L);
        assertEquals(Set.of(), roles.stream().map(Role::getCode).collect(Collectors.toSet()));
    }

    @Test
    public void testReplaceUserRoles() {
        roleService.replaceUserRoles(1L, Set.of(1L, 2L, 101L, 102L));
        Set<Role> roles = roleService.getRolesByUserId(1L);
        assertEquals(Set.of(1L, 2L, 101L, 102L), roles.stream().map(Role::getId).collect(Collectors.toSet()));
    }
}
