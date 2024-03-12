package com.hzhg.plm.service;

import com.hzhg.plm.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;
import java.util.stream.Collectors;

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
        Set<Role> roles = roleService.getRolesByUserId(1L);
        Set<String> rolesExpect = new HashSet<>(
                Arrays.asList("SYS_ADMIN", "PLM_ADMIN"));
        Assertions.assertEquals(rolesExpect, roles.stream().map(Role::getCode).collect(Collectors.toSet()));
    }
}
