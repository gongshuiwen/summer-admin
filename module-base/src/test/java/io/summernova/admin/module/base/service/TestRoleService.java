package io.summernova.admin.module.base.service;

import io.summernova.admin.core.context.BaseContextHolder;
import io.summernova.admin.module.base.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/role.sql",
        "/sql/ddl/user.sql",
        "/sql/ddl/user_role.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/user.sql",
        "/sql/test/data/user_role.sql",
})
class TestRoleService {

    @Autowired
    RoleService roleService;

    @BeforeEach
    void beforeEach() {
        assertEquals(Set.of(1L, 2L), getRoleIdsByUserId(1L));
    }

    @AfterEach
    void afterEach() {
        BaseContextHolder.clearContext();
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
