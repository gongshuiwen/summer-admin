package com.hzboiler.erp.core.security;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.security.DataAccessAuthority;
import com.hzboiler.erp.core.security.DataAccessAuthorityChecker;
import com.hzboiler.erp.core.security.DataAccessException;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestDataAccessAuthorityChecker {

    final static String AUTHORITY = "Mock:SELECT";

    static void doCheck() {
        DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT);
    }

    @AfterEach
    void afterEach() {
        BaseContextHolder.clearContext();
    }

    @Test
    void testNotAuthenticated() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithAnonymousUser
    void testUserAnonymous() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockAdmin
    void testUserAdmin() {
        assertDoesNotThrow(TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockUser
    void testUserWithoutAuthority() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY})
    void testUserWithAuthority() {
        assertDoesNotThrow(TestDataAccessAuthorityChecker::doCheck);
    }
}