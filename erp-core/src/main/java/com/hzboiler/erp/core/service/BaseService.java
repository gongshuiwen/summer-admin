package com.hzboiler.erp.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzboiler.erp.core.context.BaseContextContainer;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.mapper.RelationMapperRegistry;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.OrderBys;

import java.util.Collection;
import java.util.List;

/**
 * @param <T> type extends {@link BaseModel}
 * @author gongshuiwen
 */
public interface BaseService<T extends BaseModel> extends BaseContextContainer {

    T selectById(Long id);

    List<T> selectByIds(Collection<Long> ids);

    default IPage<T> page(Long pageNum, Long pageSize) {
        return page(pageNum, pageSize, null, null);
    }

    default IPage<T> page(Long pageNum, Long pageSize, Condition condition) {
        return page(pageNum, pageSize, condition, null);
    }

    IPage<T> page(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys);

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

    default T selectOne(Condition condition, OrderBys orderBys) {
        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());
        if (orderBys != null)
            orderBys.applyToQueryWrapper(queryWrapper);
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
    }

    default List<T> selectList(Condition condition, Long limit, Long offset, OrderBys orderBys) {
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

        if (orderBys != null)
            orderBys.applyToQueryWrapper(queryWrapper);

        return selectList(queryWrapper);
    }

    List<T> selectList(QueryWrapper<T> queryWrapper);

    List<T> nameSearch(String name);

    // ====================
    // Create methods
    // ====================

    boolean createOne(T record);

    boolean createBatch(List<T> records);

    // ====================
    // Update methods
    // ====================

    boolean updateById(Long id, T record);

    boolean updateByIds(List<Long> ids, T record);

    // ====================
    // Delete methods
    // ====================

    boolean deleteById(Long id);

    boolean deleteByIds(List<Long> ids);

    // ====================
    // Other methods
    // ====================

    Class<T> getModelClass();

    BaseMapper<T> getMapper();

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
}
