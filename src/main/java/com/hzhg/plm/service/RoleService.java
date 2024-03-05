package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.Role;

import java.util.Set;

public interface RoleService extends IService<Role> {

    Set<Role> getRolesByUserId(Long userId);
}
