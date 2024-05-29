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
    static final String MOCK_AUTHORITY_SELECT = MOCK_ENTITY_NAME + ":" + AUTHORITY_SELECT;
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_CREATE;
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_UPDATE;
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + ":" + AUTHORITY_DELETE;

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
