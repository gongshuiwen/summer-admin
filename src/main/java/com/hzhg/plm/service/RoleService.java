package com.hzhg.plm.service;

import com.hzhg.plm.core.service.IBaseService;
import com.hzhg.plm.entity.Role;

import java.util.Set;

public interface RoleService extends IBaseService<Role> {

    Set<Role> getRolesByUserId(Long userId);
}
