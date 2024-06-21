package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzboiler.erp.core.context.BaseContext;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.mapper.RelationMapperRegistry;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.protocal.query.OrderBy;
import com.hzboiler.erp.core.protocal.query.Condition;

import java.util.List;

public interface BaseService<T extends BaseModel> {

    T selectById(Long id);

    List<T> selectByIds(List<Long> ids);

    default IPage<T> page(Long pageNum, Long pageSize) {
        return page(pageNum, pageSize, null, null);
    }

    default IPage<T> page(Long pageNum, Long pageSize, String sort) {
        return page(pageNum, pageSize, sort, null);
    }

    IPage<T> page(Long pageNum, Long pageSize, String sorts, Condition condition);

    default Long count(Condition condition) {
        if (condition == null)
            return count((QueryWrapper<T>) null);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        condition.applyToQueryWrapper(queryWrapper);
        return count(queryWrapper);
    }

    Long count(QueryWrapper<T> queryWrapper);

    default T selectOne(Condition condition) {
        return selectOne(condition, null);
    }

    default T selectOne(Condition condition, List<OrderBy<T>> orderBys) {
        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());

        if (orderBys != null && !orderBys.isEmpty()) {
            for (OrderBy<T> orderBy : orderBys) {
                orderBy.applyToQueryWrapper(queryWrapper);
            }
        }
        return selectOne(queryWrapper);
    }

    default T selectOne(QueryWrapper<T> queryWrapper) {
        List<T> list = selectList(queryWrapper);
        return list.isEmpty() ? null : list.get(0);
    }

    default List<T> selectList(Condition condition) {
        return selectList(condition, 0L, 0L, null);
    }

    default List<T> selectList(Condition condition, Long limit) {
        return selectList(condition, limit, 0L, null);
    }

    default List<T> selectList(Condition condition, Long limit, Long offset) {
        return selectList(condition, limit, offset, null);
    };

    default List<T> selectList(Condition condition, Long limit, Long offset, List<OrderBy<T>> orderBys) {
        if (limit == null || limit < 0)
            throw new IllegalArgumentException("limit must not be null or < 0!");
        if (offset == null || offset < 0)
            throw new IllegalArgumentException("offset must not be null or < 0!");

        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());
        if (limit > 0) {
            queryWrapper.last("limit " + limit);
            if (offset > 0) {
                queryWrapper.last("offset " + offset);
            }
        }

        if (orderBys != null && !orderBys.isEmpty()) {
            for (OrderBy<T> orderBy : orderBys) {
                orderBy.applyToQueryWrapper(queryWrapper);
            }
        }

        return selectList(queryWrapper);
    }

    List<T> selectList(QueryWrapper<T> queryWrapper);

    List<T> nameSearch(String name);

    boolean createOne(T record);

    boolean createBatch(List<T> records);

    boolean updateById(Long id, T record);

    boolean updateByIds(List<Long> ids, T record);

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);

    /**
     * Get current {@link BaseContext}
     *
     * @return {@link BaseContext}
     */
    default BaseContext getBaseContext() {
        return BaseContextHolder.getContext();
    }

    Class<T> getModelClass();

    BaseMapper<T> getBaseMapper();

    /**
     * Returns a {@link RelationMapper} instance for the service's model class and the given target class
     *
     * @param targetClass the target class of the relation
     * @return {@link RelationMapper} instance
     */
    default RelationMapper getRelationMapper(Class<? extends BaseModel> targetClass) {
        return RelationMapperRegistry.getMapper(getModelClass(), targetClass);
    }

    /**
     * Returns a {@link RelationMapper} instance for the two given classes
     *
     * @param class1 the first class
     * @param class2 the second class
     * @return {@link RelationMapper} instance
     */
    default RelationMapper getRelationMapper(Class<? extends BaseModel> class1, Class<? extends BaseModel> class2) {
        return RelationMapperRegistry.getMapper(class1, class2);
    }

    <S extends BaseService<AT>, AT extends BaseModel> S getService(Class<AT> modelClass);

    /**
     * copied from mybatis-plus {@link IService#lambdaQuery()}
     * <p>
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    LambdaQueryChainWrapper<T> lambdaQuery();

    /**
     * copied from mybatis-plus {@link IService#lambdaUpdate()}
     * <p>
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaUpdateWrapper 的包装类
     */
    LambdaUpdateChainWrapper<T> lambdaUpdate();
}
