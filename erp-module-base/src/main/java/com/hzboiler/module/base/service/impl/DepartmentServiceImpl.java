package com.hzboiler.module.base.service.impl;

import com.hzboiler.module.base.model.Department;
import com.hzboiler.module.base.mapper.DepartmentMapper;
import com.hzboiler.core.service.AbstractBaseService;
import com.hzboiler.module.base.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DepartmentServiceImpl extends AbstractBaseService<DepartmentMapper, Department> implements DepartmentService {
}
