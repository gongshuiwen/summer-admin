package com.hzboiler.core.protocal;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public class OrderBy<T> {

    private String column;
    private Type type;

    public void applyToQueryWrapper(QueryWrapper<T> queryWrapper) {
        checkColumn();
        if (type == null || type == Type.ASC) {
            queryWrapper.orderByAsc(column);
        } else {
            queryWrapper.orderByDesc(column);
        }
    }

    private void checkColumn() {
        if (column == null) {
            throw new IllegalArgumentException("Invalid column: column cannot be null");
        }

        // TODO: implement check column existing in entity class with cache
    }

    enum Type {
        ASC,
        DESC
    }
}
