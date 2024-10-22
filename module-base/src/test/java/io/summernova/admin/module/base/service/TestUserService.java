package io.summernova.admin.module.base.service;

import io.summernova.admin.module.base.model.Role;
import io.summernova.admin.module.base.model.User;
import io.summernova.admin.test.annotation.SummerAdminTest;
import io.summernova.admin.test.annotation.WithMockAdmin;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@SummerAdminTest
class TestUserService {

    RoleService roleService = new RoleServiceImpl();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserServiceImpl(roleService, passwordEncoder);

    TestUserService() throws NoSuchFieldException {
    }

    @BeforeEach
    void beforeEach() {
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/user.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "io/summernova/admin/module/base/sql/ddl/user_role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/user.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/role.sql");
        ScriptRunnerUtil.runScript(SqlSessionUtil.getSqlSession(), "sql/test/data/user_role.sql");
    }

    @Test
    @WithMockAdmin
    void testLoadUserByUsername() {
        assertEquals("admin", userService.loadUserByUsername("admin").getUsername());
        assertEquals("demo", userService.loadUserByUsername("demo").getUsername());
        assertNull(userService.loadUserByUsername("xxx"));
    }

    @Test
    @WithMockAdmin
    void testCreateOne() {
        // Create user
        User userCreate = new User();
        userCreate.setUsername("test");
        userCreate.setNickname("test");
        userCreate.setPassword("123456");
        userService.createOne(userCreate);

        // Test user
        User user = userService.loadUserByUsername("test");
        assertNotNull(user.getId());

        // Test password
        assertTrue(passwordEncoder.matches("123456", user.getPassword()));

        // Test roles
        assertRoles(user, "BASE_USER");
    }

    private void assertRoles(User user, String... roleCodes) {
        Set<String> roles = new HashSet<>(List.of(roleCodes));
        assertEquals(roles, roleService.getRolesByUserId(user.getId()).stream().map(Role::getCode).collect(Collectors.toSet()));
    }

    @Test
    @WithMockAdmin
    void testUpdatePasswordAdmin() {
        // Get user
        User userDemo = userService.loadUserByUsername("demo");

        // Change password
        String newPassword = "12345678";
        User userUpdate = new User();
        userUpdate.setPassword(newPassword);
        userService.updateById(userDemo.getId(), userUpdate);

        // Test password
        assertTrue(passwordEncoder.matches(newPassword, userService.selectById(userDemo.getId()).getPassword()));
    }

//    @Test
//    @WithMockUser(username = "demo", authorities = {CODE_BASE_USER, "S:User", "U:User"})
//    void testUpdatePasswordDemo() {
//        String newPassword = "12345678";
//
//        // Get user
//        User userAdmin = userService.loadUserByUsername("admin");
//        User userDemo = userService.loadUserByUsername("demo");
//
//        // Change password of demo
//        User userUpdate1 = new User();
//        userUpdate1.setPassword(newPassword);
//        assertDoesNotThrow(() -> userService.updateById(userDemo.getId(), userUpdate1));
//        assertTrue(passwordEncoder.matches(newPassword, userService.selectById(userDemo.getId()).getPassword()));
//
//        // Change password of admin
//        assertThrows(BusinessException.class, () -> userService.updateById(userAdmin.getId(), userUpdate1));
//
//        // Change nickname of demo
//        User userUpdate2 = new User();
//        userUpdate2.setNickname("xxx");
//        assertDoesNotThrow(() -> userService.updateById(userDemo.getId(), userUpdate2));
//        assertEquals("xxx", userService.selectById(userDemo.getId()).getNickname());
//
//        // Change nickname of admin
//        assertThrows(BusinessException.class, () -> userService.updateById(userAdmin.getId(), userUpdate2));
//    }
}
