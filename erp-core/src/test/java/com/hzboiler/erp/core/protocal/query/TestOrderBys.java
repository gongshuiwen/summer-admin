package com.hzboiler.erp.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzboiler.erp.core.model.Mock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestOrderBys {

    @Test
    void testApplyToQueryWrapper() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBys.of(OrderBy.asc("name"), OrderBy.desc("id")).applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }
}
