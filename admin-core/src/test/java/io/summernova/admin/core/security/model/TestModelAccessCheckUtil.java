package io.summernova.admin.core.security.model;

import io.summernova.admin.core.annotaion.WithMockAdmin;
import io.summernova.admin.core.annotaion.WithMockUser;
import io.summernova.admin.core.context.BaseContextExtension;
import io.summernova.admin.core.context.BaseContextHolder;
import io.summernova.admin.core.domain.model.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestModelAccessCheckUtil {

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
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser
    void testCheckWithoutAnyAuthority() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_SELECT})
    void testCheckWithAuthoritySelect() {
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_CREATE})
    void testCheckWithAuthorityCreate() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_UPDATE})
    void testCheckWithAuthorityUpdate() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_DELETE})
    void testCheckWithAuthorityDelete() {
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> ModelAccessCheckUtil.checkUpdate(Mock.class));
        assertDoesNotThrow(() -> ModelAccessCheckUtil.checkDelete(Mock.class));
    }
}
