package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.Mock;
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

    @Test
    void testStaticMethodOf1() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBys.of(OrderBy.asc("name"), OrderBy.desc("id")).applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testStaticMethodOf2() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBys.of("name", "_id").applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }

    @Test
    void testStaticMethodParse() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        OrderBys.parse("name,_id").applyToQueryWrapper(mockQueryWrapper);
        assertEquals(" ORDER BY name ASC,id DESC", mockQueryWrapper.getSqlSegment());
    }
}
