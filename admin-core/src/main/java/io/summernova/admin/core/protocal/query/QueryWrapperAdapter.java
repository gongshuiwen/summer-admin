package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.BaseModel;

/**
 * @author gongshuiwen
 */
public interface QueryWrapperAdapter {

    default <T extends BaseModel> QueryWrapper<T> toQueryWrapper(Class<T> clazz) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntityClass(clazz);
        applyToQueryWrapper(queryWrapper);
        return queryWrapper;
    }

    void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper);
}
