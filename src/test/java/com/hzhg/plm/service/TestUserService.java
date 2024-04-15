package com.hzhg.plm.service;

import com.hzhg.plm.core.annotaion.WithMockAdmin;
import com.hzhg.plm.entity.Permission;
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
    PermissionService permissionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testLoadUserByUsername() {
        User user = userService.loadUserByUsername("admin");
        assertEquals("admin", user.getUsername());

        // Test Roles
        Set<String> roles = new HashSet<>(List.of("SYS_ADMIN", "BASE_USER"));
        assertEquals(roles, user.getRoles().get().stream().map(Role::getCode).collect(Collectors.toSet()));

        // Test Authorities
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SYS_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_BASE_USER"));
        Set<Long> roleIds = user.getRoles().get().stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
        permissions.forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm.getCode())));
        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    @WithMockAdmin
    public void testCreateOne() {
        // Create user
        User userCreate = new User();
        userCreate.setUsername("test");
        userCreate.setNickname("test");
        userCreate.setPassword("123456");
        userService.createOne(userCreate);

        User user = userService.loadUserByUsername("test");
        assertNotNull(user.getId());

        // Test Password
        assertTrue(passwordEncoder.matches("123456", user.getPassword()));

        // Test Roles
        Set<String> roles = new HashSet<>(List.of("BASE_USER"));
        assertEquals(roles, user.getRoles().get().stream().map(Role::getCode).collect(Collectors.toSet()));

        // Test Authorities
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BASE_USER"));
        Set<Long> roleIds = user.getRoles().get().stream().map(Role::getId).collect(Collectors.toSet());
        Set<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
        permissions.forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm.getCode())));
        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    @WithMockAdmin
    public void testChangePassword() {
        // Create user
        User userCreate = new User();
        userCreate.setUsername("test");
        userCreate.setPassword("123456");
        userService.createOne(userCreate);

        // Change Password
        String newPassword = "12345678";
        User userUpdate = new User();
        userUpdate.setPassword(newPassword);
        userService.updateById(userCreate.getId(), userUpdate);

        // Test Password
        assertTrue(passwordEncoder.matches(newPassword, userService.selectById(userCreate.getId()).getPassword()));
    }
}
