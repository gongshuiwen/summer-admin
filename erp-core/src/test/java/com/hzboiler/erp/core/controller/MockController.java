package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.model.Mock;
import com.hzboiler.erp.core.service.MockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gongshuiwen
 */
@RestController
@RequestMapping("/mock")
class MockController extends BaseController<MockService, Mock> {
}
