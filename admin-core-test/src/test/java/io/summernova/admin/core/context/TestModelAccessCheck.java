package io.summernova.admin.core.context;

import io.summernova.admin.core.domain.model.Mock;
import io.summernova.admin.core.security.model.ModelAccessException;
import io.summernova.admin.test.annotation.WithMockAdmin;
import io.summernova.admin.test.annotation.WithMockUser;
import io.summernova.admin.test.context.BaseContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author gongshuiwen
 */
@ExtendWith(BaseContextExtension.class)
class TestModelAccessCheck {

    final static String AUTHORITY_SELECT = "S:Mock";
    final static String AUTHORITY_CREATE = "C:Mock";
    final static String AUTHORITY_UPDATE = "U:Mock";
    final static String AUTHORITY_DELETE = "D:Mock";

    @Test
    @WithMockAdmin
    void testAdmin() {
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }

    @Test
    @WithMockUser
    void testCheckWithoutAnyAuthority() {
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_SELECT})
    void testCheckWithAuthoritySelect() {
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_CREATE})
    void testCheckWithAuthorityCreate() {
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_UPDATE})
    void testCheckWithAuthorityUpdate() {
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }

    @Test
    @WithMockUser(authorities = {AUTHORITY_DELETE})
    void testCheckWithAuthorityDelete() {
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelSelect(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelCreate(Mock.class));
        assertThrows(ModelAccessException.class, () -> BaseContextHolder.getContext().checkModelUpdate(Mock.class));
        assertDoesNotThrow(() -> BaseContextHolder.getContext().checkModelDelete(Mock.class));
    }
}
