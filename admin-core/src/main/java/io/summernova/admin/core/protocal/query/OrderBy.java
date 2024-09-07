package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public class OrderBy implements QueryWrapperAdapter {

    private final String field;
    private final OrderByType type;

    // prevent external instantiation
    private OrderBy(String field, OrderByType type) {
        this.field = field;
        this.type = type;
    }

    public static OrderBy asc(String field) {
        FieldNameCheckUtil.checkFieldName(field);
        return new OrderBy(field, OrderByType.ASC);
    }

    public static OrderBy desc(String field) {
        FieldNameCheckUtil.checkFieldName(field);
        return new OrderBy(field, OrderByType.DESC);
    }

    @Override
    public void applyToQueryWrapper(QueryWrapper<? extends BaseModel> queryWrapper) {
        if (type == OrderByType.ASC) {
            queryWrapper.orderByAsc(field);
        } else if (type == OrderByType.DESC) {
            queryWrapper.orderByDesc(field);
        } else {
            throw new IllegalArgumentException("Invalid order by type: " + type);
        }
    }

    private enum OrderByType {
        ASC,
        DESC
    }
}
