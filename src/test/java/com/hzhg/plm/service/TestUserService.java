package com.hzhg.plm.service;

import com.hzhg.plm.core.annotaion.WithMockAdmin;
import com.hzhg.plm.entity.Role;
import com.hzhg.plm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {
        "/sql/ddl/user.sql",
        "/sql/ddl/role.sql",
        "/sql/ddl/permission.sql",
        "/sql/ddl/user_role.sql",
        "/sql/ddl/role_permission.sql",
        "/sql/test/data/user.sql",
        "/sql/test/data/role.sql",
        "/sql/test/data/permission.sql",
        "/sql/test/data/user_role.sql",
        "/sql/test/data/role_permission.sql",
})
public class TestUserService {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testLoadUserByUsername() {
        User user = userService.loadUserByUsername("admin");
        assertEquals("admin", user.getUsername());

        // Test Roles
        Set<String> roles = new HashSet<>(Arrays.asList("SYS_ADMIN", "BASE_USER"));
        assertEquals(roles, user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()));

        // Test Authorities
        assertEquals(Set.of(
                new SimpleGrantedAuthority("ROLE_SYS_ADMIN"),
                new SimpleGrantedAuthority("ROLE_BASE_USER"),
                new SimpleGrantedAuthority("User:SELECT"),
                new SimpleGrantedAuthority("User:UPDATE")
        ), user.getAuthorities());
    }

    @Test
    @WithMockAdmin
    public void testCreateOne() {
        User user = new User();
        user.setUsername("test");
        user.setNickname("test");
        user.setPassword("123456");

        userService.createOne(user);

        user = userService.loadUserByUsername("test");
        assertNotNull(user.getId());
        assertTrue(passwordEncoder.matches("123456", user.getPassword()));
        assertEquals(Set.of("BASE_USER"), user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()));
        assertEquals(Set.of(
                new SimpleGrantedAuthority("ROLE_BASE_USER"),
                new SimpleGrantedAuthority("User:SELECT"),
                new SimpleGrantedAuthority("User:UPDATE")
        ), user.getAuthorities());
    }
}
