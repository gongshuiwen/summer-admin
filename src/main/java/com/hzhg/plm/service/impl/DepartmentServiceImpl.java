package com.hzhg.plm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzhg.plm.entity.Department;
import com.hzhg.plm.mapper.DepartmentMapper;
import com.hzhg.plm.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> getDepartmentsByParentId(Long parentId) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getParentId, parentId);
        queryWrapper.orderByAsc(Department::getOrderNum);
        return list(queryWrapper);
    }
}
