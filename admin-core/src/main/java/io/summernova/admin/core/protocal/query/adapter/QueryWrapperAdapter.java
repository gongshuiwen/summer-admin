package io.summernova.admin.core.protocal.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author gongshuiwen
 */
public interface QueryWrapperAdapter<T> {

    default <M> QueryWrapper<M> toQueryWrapper(T object, Class<M> clazz) {
        QueryWrapper<M> queryWrapper = new QueryWrapper<>(clazz);
        applyToQueryWrapper(object, queryWrapper);
        return queryWrapper;
    }

    <M> void applyToQueryWrapper(T object, QueryWrapper<M> queryWrapper);
}
