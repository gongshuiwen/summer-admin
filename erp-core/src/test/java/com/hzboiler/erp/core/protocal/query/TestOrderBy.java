package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderBy {

    @Test
    void testApplyToQueryWrapperAsc() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBy.asc("name").applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testApplyToQueryWrapperDesc() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBy.desc("name").applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name DESC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testApplyToQueryWrapperComposition() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBy.asc("name").applyToQueryWrapper(mockQueryWrapper);
        OrderBy.desc("id").applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }
}
