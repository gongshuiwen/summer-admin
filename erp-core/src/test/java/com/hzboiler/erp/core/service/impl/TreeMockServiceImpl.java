package com.hzboiler.erp.core.service.impl;

import com.hzboiler.erp.core.mapper.TreeMockMapper;
import com.hzboiler.erp.core.model.TreeMock;
import com.hzboiler.erp.core.service.AbstractBaseTreeService;
import com.hzboiler.erp.core.service.TreeMockService;
import org.springframework.stereotype.Service;

@Service
public class TreeMockServiceImpl extends AbstractBaseTreeService<TreeMockMapper, TreeMock> implements TreeMockService {
}
