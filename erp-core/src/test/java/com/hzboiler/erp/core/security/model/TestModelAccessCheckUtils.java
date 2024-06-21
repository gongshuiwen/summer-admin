package com.hzboiler.erp.core.security.model;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestModelAccessCheckUtils {

    final static String AUTHORITY_SELECT = "S:Mock";
    final static String AUTHORITY_CREATE = "C:Mock";
    final static String AUTHORITY_UPDATE = "U:Mock";
    final static String AUTHORITY_DELETE = "D:Mock";

    @AfterEach
    void afterEach() {
        BaseContextHolder.clearContext();
    }

    @Test
    @WithMockAdmin
    void testAdmin() {
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser
    void testCheckWithoutAnyAuthority() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_SELECT})
    void testCheckWithAuthoritySelect() {
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_CREATE})
    void testCheckWithAuthorityCreate() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_UPDATE})
    void testCheckWithAuthorityUpdate() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_DELETE})
    void testCheckWithAuthorityDelete() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtils.checkUpdate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtils.checkDelete(Mock.class));
    }
}
