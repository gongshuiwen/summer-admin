package com.hzboiler.core.service.impl;

import com.hzboiler.core.entity.Mock;
import com.hzboiler.core.mapper.MockMapper;
import com.hzboiler.core.service.AbstractBaseService;
import com.hzboiler.core.service.MockService;
import org.springframework.stereotype.Service;

@Service
public class MockServiceImpl extends AbstractBaseService<MockMapper, Mock> implements MockService {
}
