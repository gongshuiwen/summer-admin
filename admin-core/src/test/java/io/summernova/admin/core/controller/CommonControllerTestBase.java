package io.summernova.admin.core.controller;

import io.summernova.admin.core.annotaion.WithMockAdmin;
import io.summernova.admin.core.service.MockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static io.summernova.admin.common.exception.CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED;
import static io.summernova.admin.common.exception.CoreBusinessExceptionEnums.ERROR_AUTHENTICATION_FAILED;
import static io.summernova.admin.core.security.Constants.*;

/**
 * @author gongshuiwen
 */
abstract class CommonControllerTestBase extends ControllerTestBase {

    static final String MOCK_PATH = "/common/mock";
    static final String MOCK_MODEL_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK_MODEL_NAME;
    static final String MOCK_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK_MODEL_NAME;
    static final String MOCK_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK_MODEL_NAME;
    static final String MOCK_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK_MODEL_NAME;

    @Autowired
    MockService mockService;

    @Test
    @WithAnonymousUser
    void testAnonymous() throws Exception {
        checkResultActionsException(doExample(), ERROR_AUTHENTICATION_FAILED);
    }

    @Test
    @WithMockUser
    void testNotAuthorized() throws Exception {
        checkResultActionsException(doExample(), ERROR_ACCESS_DENIED);
    }

    @Test
    @WithMockAdmin
    void testAdmin() throws Exception {
        testAuthorized();
    }

    abstract ResultActions doExample() throws Exception;

    abstract void testAuthorized() throws Exception;
}
