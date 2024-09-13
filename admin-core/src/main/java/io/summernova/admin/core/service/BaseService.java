package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.summernova.admin.core.context.BaseContextContainer;
import io.summernova.admin.core.mapper.BaseMapper;
import io.summernova.admin.core.mapper.RelationMapper;
import io.summernova.admin.core.mapper.RelationMapperRegistry;
import io.summernova.admin.core.model.BaseModel;
import io.summernova.admin.core.protocal.query.Condition;
import io.summernova.admin.core.protocal.query.OrderBys;
import io.summernova.admin.core.protocal.query.adapter.ConditionQueryWrapperAdapter;
import io.summernova.admin.core.protocal.query.adapter.OrderBysQueryWrapperAdapter;
import org.apache.ibatis.session.SqlSession;

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
        ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(condition, queryWrapper);
        return count(queryWrapper);
    }

    Long count(QueryWrapper<T> queryWrapper);

    default T selectOne(Condition condition) {
        return selectOne(condition, null);
    }

    default T selectOne(Condition condition, OrderBys orderBys) {
        QueryWrapper<T> queryWrapper = condition.toQueryWrapper(getModelClass());
        if (orderBys != null)
            OrderBysQueryWrapperAdapter.applyOrderBysToQueryWrapper(orderBys, queryWrapper);
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
            OrderBysQueryWrapperAdapter.applyOrderBysToQueryWrapper(orderBys, queryWrapper);

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
    SqlSession getSqlSession();

    Class<T> getModelClass();

    Class<BaseMapper<T>> getBaseMapperClass();

    BaseMapper<T> getBaseMapper();

    <AT extends BaseModel> BaseMapper<AT> getBaseMapper(Class<AT> modelClass);

    <M extends BaseMapper<T>> M getMapper(Class<M> mapperInterface);

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
