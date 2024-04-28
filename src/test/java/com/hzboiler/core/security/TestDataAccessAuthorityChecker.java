package com.hzboiler.core.security;

import com.hzboiler.core.annotaion.WithMockAdmin;
import com.hzboiler.core.entity.Mock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestDataAccessAuthorityChecker {

    final static String AUTHORITY = "Mock:SELECT";

    static void doCheck() {
        DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT);
    }

    @Test
    public void testNotAuthenticated() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithAnonymousUser
    public void testUserAnonymous() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockAdmin
    public void testUserAdmin() {
        assertDoesNotThrow(TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockUser
    public void testUserWithoutAuthority() {
        assertThrows(DataAccessException.class, TestDataAccessAuthorityChecker::doCheck);
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY})
    public void testUserWithAuthority() {
        assertDoesNotThrow(TestDataAccessAuthorityChecker::doCheck);
    }
}