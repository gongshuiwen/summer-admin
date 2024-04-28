package com.hzboiler.core.controller;

import com.hzboiler.core.service.MockService;
import com.hzboiler.core.entity.Mock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mock")
public class MockController extends BaseController<MockService, Mock> {
}
