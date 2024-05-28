package com.hzboiler.erp.core.controller;

import com.hzboiler.erp.core.controller.BaseController;
import com.hzboiler.erp.core.service.MockService;
import com.hzboiler.erp.core.model.Mock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
class MockController extends BaseController<MockService, Mock> {
}
