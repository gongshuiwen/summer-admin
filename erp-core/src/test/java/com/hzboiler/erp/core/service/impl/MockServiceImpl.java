package com.hzboiler.erp.core.service.impl;

import com.hzboiler.erp.core.mapper.MockMapper;
import com.hzboiler.erp.core.model.Mock;
import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.core.service.MockService;
import org.springframework.stereotype.Service;

@Service
public class MockServiceImpl extends AbstractBaseService<MockMapper, Mock> implements MockService {
}
