package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.annotaion.WithMockAdmin;
import com.hzboiler.erp.core.mapper.MockMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED;
import static com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums.ERROR_AUTHENTICATION_FAILED;
import static com.hzboiler.erp.core.security.Constants.*;

/**
 * @author gongshuiwen
 */
abstract class MockControllerTestBase extends ControllerTestBase {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = AUTHORITY_SELECT_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_CREATE = AUTHORITY_CREATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_UPDATE = AUTHORITY_UPDATE_CODE_PREFIX + MOCK_ENTITY_NAME;
    static final String MOCK_AUTHORITY_DELETE = AUTHORITY_DELETE_CODE_PREFIX + MOCK_ENTITY_NAME;

    @Autowired
    MockMapper mockMapper;

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
