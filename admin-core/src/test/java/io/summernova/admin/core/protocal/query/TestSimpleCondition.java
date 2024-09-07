package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.Mock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestSimpleCondition {

    static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    @Test
    void testApplyToQueryWrapperGeneral() {
        QueryWrapper<Mock> mockQueryWrapper;
        Condition condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id = #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "<", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id < #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", ">", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id > #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "!=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id <> #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", "<=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id <= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("id", ">=", 100);
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(id >= #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals(100, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperLike() {
        QueryWrapper<Mock> mockQueryWrapper;
        Condition condition;

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "like", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "likeLeft", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "likeRight", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLike", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLikeLeft", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("%mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));

        mockQueryWrapper = new QueryWrapper<>();
        condition = SimpleCondition.of("name", "notLikeRight", "mock");
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(name NOT LIKE #{ew.paramNameValuePairs.MPGENVAL1})", mockQueryWrapper.getSqlSegment());
        assertEquals("mock%", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testGetSql() {
        Condition condition = SimpleCondition.of("name", "like", "mock");
        assertEquals("name LIKE 'mock'", condition.getSql());
    }
}