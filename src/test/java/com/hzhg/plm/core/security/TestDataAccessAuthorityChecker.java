package com.hzhg.plm.core.security;

import com.hzhg.plm.core.entity.Mock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestDataAccessAuthorityChecker {


    @Test
    public void testNotAuthenticated() {
        assertThrows(DataAccessException.class, () -> DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT));
    }

    @Test
    @WithAnonymousUser
    public void testAnonymousUser() {
        assertThrows(DataAccessException.class, () -> DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT));
    }

    @Test
    @WithMockUser(roles = {})
    public void testUserDoesNotHaveAuthority() {
        assertThrows(DataAccessException.class, () -> DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT));
    }

    @Test
    @WithMockUser(authorities = {"Mock:SELECT"})
    public void testUserHasAuthority() {
        assertDoesNotThrow(() -> DataAccessAuthorityChecker.check(Mock.class, DataAccessAuthority.SELECT));
    }
}