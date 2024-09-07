package io.summernova.admin.module.base.service;

import io.summernova.admin.core.service.BaseService;
import io.summernova.admin.module.base.model.Role;

import java.util.List;
import java.util.Set;

/**
 * @author gongshuiwen
 */
public interface RoleService extends BaseService<Role> {

    Set<Role> getRolesByUserId(Long userId);

    Role getRoleByCode(String code);

    List<Role> getDefaultRoles();

    void addUserRoles(Long userId, Set<Long> roleIds);

    void removeUserRoles(Long userId, Set<Long> roleIds);

    void replaceUserRoles(Long userId, Set<Long> roleIds);
}
