package com.hzhg.plm.service.impl;

import com.hzhg.plm.core.service.AbstractBaseService;
import com.hzhg.plm.entity.Department;
import com.hzhg.plm.mapper.DepartmentMapper;
import com.hzhg.plm.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DepartmentServiceImpl extends AbstractBaseService<DepartmentMapper, Department> implements DepartmentService {
}
