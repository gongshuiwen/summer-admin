package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    public List<Department> getDepartmentsByParentId(Long parentId);
}
