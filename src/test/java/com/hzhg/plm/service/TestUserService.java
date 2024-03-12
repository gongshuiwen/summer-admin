package com.hzhg.plm.service;

import com.hzhg.plm.entity.Role;
import com.hzhg.plm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/user.sql",
        "/sql/ddl/role.sql",
        "/sql/ddl/user_role.sql",
        "/sql/test/data/user.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/user_role.sql",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TestUserService {

    @Autowired
    UserService userService;

    @Test

    public void testLoadUserByUsername() {
        User user = userService.loadUserByUsername("admin");
        Assertions.assertEquals("admin", user.getUsername());

        // Tet Roles
        Set<String> roles = new HashSet<>(Arrays.asList( "SYS_ADMIN", "PLM_ADMIN"));
        Assertions.assertEquals(roles, user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()));

        // Test Authorities
        Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(
            new SimpleGrantedAuthority("ROLE_SYS_ADMIN"),
            new SimpleGrantedAuthority("ROLE_PLM_ADMIN"),
            new SimpleGrantedAuthority("User:SELECT"),
            new SimpleGrantedAuthority("User:CREATE"),
            new SimpleGrantedAuthority("User:UPDATE"),
            new SimpleGrantedAuthority("User:DELETE")
        ));
        Assertions.assertEquals(authorities, user.getAuthorities());
    }
}
