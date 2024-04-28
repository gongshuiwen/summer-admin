package com.hzboiler.base.service.impl;

import com.hzboiler.base.entity.Department;
import com.hzboiler.base.mapper.DepartmentMapper;
import com.hzboiler.core.service.AbstractBaseService;
import com.hzboiler.base.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DepartmentServiceImpl extends AbstractBaseService<DepartmentMapper, Department> implements DepartmentService {
}
