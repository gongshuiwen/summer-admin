package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class Query implements QueryWrapperAdapter {

    private final Long pageNum;
    private final Long pageSize;
    private final Condition condition;
    private final OrderBys orderBys;

    // prevent external instantiation
    private Query(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.condition = condition;
        this.orderBys = orderBys;
    }

    public static Query of(Long pageNum, Long pageSize, Condition condition, OrderBys orderBys) {
        return new Query(pageNum, pageSize, condition, orderBys);
    }

    public <T extends BaseModel> IPage<T> getPage() {
        return new Page<>(pageNum, pageSize);
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (condition != null)
            condition.applyToQueryWrapper(queryWrapper);
        if (orderBys != null)
            orderBys.applyToQueryWrapper(queryWrapper);
    }
}
