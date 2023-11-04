package com.hzhg.plm.core.controller;

import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.service.MockService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mock")
public class MockController extends BaseController<MockService, Mock> {
}
