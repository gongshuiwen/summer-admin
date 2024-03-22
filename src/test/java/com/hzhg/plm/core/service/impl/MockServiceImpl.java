package com.hzhg.plm.core.service.impl;

import com.hzhg.plm.core.entity.Mock;
import com.hzhg.plm.core.mapper.MockMapper;
import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.core.service.MockService;
import org.springframework.stereotype.Service;

@Service
public class MockServiceImpl extends AbstractBaseService<MockMapper, Mock> implements MockService {
}
