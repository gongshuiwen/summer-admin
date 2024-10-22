package io.summernova.admin.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.summernova.admin.common.query.Condition;
import io.summernova.admin.common.query.OrderBys;
import io.summernova.admin.core.context.BaseContextContainer;
import io.summernova.admin.core.dal.mapper.BaseMapper;
import io.summernova.admin.core.dal.mapper.RelationMapper;
import io.summernova.admin.core.dal.query.adapter.ConditionQueryWrapperAdapter;
import io.summernova.admin.core.dal.query.adapter.OrderBysQueryWrapperAdapter;
import io.summernova.admin.core.domain.model.BaseModel;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @param <T> type extends {@link BaseModel}
 * @author gongshuiwen
 */
public interface BaseService<T extends BaseModel> extends BaseContextContainer {

    // ====================
    // Select methods
    // ====================
    T selectById(Long id);

    List<T> selectByIds(Collection<Long> ids);

    default IPage<T> selectPage(Long pageNum, Long pageSize) {
        return selectPage(pageNum, pageSize, null, null);
    }

    default IPage<T> selectPage(Long pageNum, Long pageSize, Condition condition) {
        return selectPage(pageNum, pageSize, condition, null);
    }

    IPage<T> selectPage(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys);

    default Long selectCount(Condition condition) {
        if (condition == null) return selectCount((QueryWrapper<T>) null);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        ConditionQueryWrapperAdapter.applyConditionToQueryWrapper(condition, queryWrapper);
        return selectCount(queryWrapper);
    }

    Long selectCount(QueryWrapper<T> queryWrapper);

    default T selectOne(Condition condition) {
        return selectOne(condition, null);
    }

    default T selectOne(Condition condition, OrderBys orderBys) {
        QueryWrapper<T> queryWrapper = ConditionQueryWrapperAdapter
                .transformConditionToQueryWrapper(condition, getModelClass());
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

        QueryWrapper<T> queryWrapper = ConditionQueryWrapperAdapter
                .transformConditionToQueryWrapper(condition, getModelClass());
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
    Class<T> getModelClass();

    default SqlSession getSqlSession() {
        return getContext().getSqlSession();
    }

    /**
     * Get the {@link BaseMapper} class of this service
     *
     * @return {@link BaseMapper} class
     */
    Class<BaseMapper<T>> getBaseMapperClass();

    /**
     * Get the {@link BaseMapper} instance of this service
     *
     * @return {@link BaseMapper} instance
     */
    BaseMapper<T> getBaseMapper();

    /**
     * Get the instance for the given mapper interface
     *
     * @param mapperInterface the mapper interface
     * @param <M>             the mapper interface type
     * @return {@link BaseMapper} instance
     */
    default <M extends BaseMapper<T>> M getMapper(Class<M> mapperInterface) {
        return getContext().getMapper(mapperInterface);
    }

    /**
     * Get the {@link BaseMapper} instance for the given model class
     *
     * @param modelClass the model class
     * @param <A>        the model class type
     * @return {@link BaseMapper} instance
     */
    default <A extends BaseModel> BaseMapper<A> getBaseMapper(Class<A> modelClass) {
        return getContext().getBaseMapper(modelClass);
    }

    /**
     * Get the {@link RelationMapper} instance for the given many2many field
     *
     * @param field the many2many field
     * @return {@link RelationMapper} instance
     */
    default RelationMapper getRelationMapper(Field field) {
        return getContext().getRelationMapper(field);
    }

    <S extends BaseService<AT>, AT extends BaseModel> S getService(Class<AT> modelClass);

    /**
     * copied from mybatis-plus IService#lambdaQuery()
     * <p>
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaQueryWrapper 的包装类
     */
    LambdaQueryChainWrapper<T> lambdaQuery();
}
