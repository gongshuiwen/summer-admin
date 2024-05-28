package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.mapper.MockMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hzboiler.erp.core.security.Constants.*;

/**
 * @author gongshuiwen
 */
class MockControllerTestBase extends ControllerTestBase {

    static final String MOCK_PATH = "/mock";
    static final String MOCK_ENTITY_NAME = "Mock";
    static final String MOCK_AUTHORITY_SELECT = MOCK_ENTITY_NAME + ":" + AUTHORITY_SELECT;
    static final String MOCK_AUTHORITY_CREATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_CREATE;
    static final String MOCK_AUTHORITY_UPDATE = MOCK_ENTITY_NAME + ":" + AUTHORITY_UPDATE;
    static final String MOCK_AUTHORITY_DELETE = MOCK_ENTITY_NAME + ":" + AUTHORITY_DELETE;

    @Autowired
    MockMapper mockMapper;
}
