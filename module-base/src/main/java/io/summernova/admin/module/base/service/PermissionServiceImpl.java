package io.summernova.admin.module.base.service;

import io.summernova.admin.core.dal.mapper.RelationMapper;
import io.summernova.admin.core.dal.mapper.RelationMapperRegistry;
import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.module.base.model.Permission;
import io.summernova.admin.module.base.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gongshuiwen
 */
@Slf4j
@Service
public class PermissionServiceImpl extends AbstractBaseService<Permission> implements PermissionService {

    private final RelationMapper rolePermissionMapper;

    // TODO: this is a temporary solution, because the sqlSession filed in AbstractBaseService has not been autowired when in constructor,
    //  so we use @Autowired @Qualifier("sqlSessionTemplate") to inject it
    public PermissionServiceImpl(
            @Autowired @Qualifier("sqlSessionTemplate") SqlSession sqlSession
    ) throws NoSuchFieldException {
        this.rolePermissionMapper = RelationMapperRegistry.getRelationMapper(
                sqlSession, Role.class.getDeclaredField("permissions"));
    }

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleId(Long roleId) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        List<Long> permIds = rolePermissionMapper.getTargetIds(roleId);
        return getBaseMapper().selectBatchIds(permIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional
    public Set<Permission> getPermissionsByRoleIds(Set<Long> roleIds) {
        Objects.requireNonNull(roleIds, "roleIds must not be null");
        if (roleIds.isEmpty()) throw new IllegalArgumentException("roleIds must not be empty");

        List<Long> permIds = rolePermissionMapper.getTargetIds(roleIds.stream().toList());
        if (permIds.isEmpty())
            return Set.of();

        return getBaseMapper().selectBatchIds(permIds).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional
    public void addRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        if (permIds.isEmpty()) throw new IllegalArgumentException("permIds must not be empty");
        rolePermissionMapper.add(roleId, permIds.stream().toList());
    }

    @Override
    @Transactional
    public void removeRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        if (permIds.isEmpty()) throw new IllegalArgumentException("permIds must not be empty");
        rolePermissionMapper.remove(roleId, permIds.stream().toList());
    }

    @Override
    @Transactional
    public void replaceRolePermissions(Long roleId, Set<Long> permIds) {
        Objects.requireNonNull(roleId, "roleId must not be null");
        Objects.requireNonNull(permIds, "permIds must not be null");
        rolePermissionMapper.replace(roleId, permIds.stream().toList());
    }
}
