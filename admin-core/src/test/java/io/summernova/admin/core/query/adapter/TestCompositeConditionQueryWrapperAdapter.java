package io.summernova.admin.core.query.adapter;

import io.summernova.admin.common.query.CompositeCondition;
import io.summernova.admin.common.query.Condition;
import io.summernova.admin.common.query.CompareCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompositeConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<CompositeConditionQueryWrapperAdapter> {

    static final LocalDateTime CREATE_TIME_START = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    static final LocalDateTime CREATE_TIME_END = LocalDateTime.of(2022, 1, 1, 0, 0, 0);

    static final List<Condition> QUERY_CONDITIONS = List.of(
            CompareCondition.eq("name", "mock"),
            CompareCondition.le("age", 18),
            CompareCondition.gt("create_time", CREATE_TIME_START)
    );

    @Test
    void testApplyToQueryWrapperCompositeAnd() {
        CompositeCondition condition = CompositeCondition.and(QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "(age <= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "(create_time > #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(18, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(CREATE_TIME_START, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeOr() {
        CompositeCondition condition = CompositeCondition.or(QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(age <= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "OR " +
                "(create_time > #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(18, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(CREATE_TIME_START, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNot() {
        CompositeCondition condition = CompositeCondition.not(CompareCondition.eq("name", "mock"));
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNotAnd() {
        CompositeCondition condition = CompositeCondition.notAnd(QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "NOT (" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "(age <= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "(create_time > #{ew.paramNameValuePairs.MPGENVAL3})))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(18, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(CREATE_TIME_START, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNotOr() {
        CompositeCondition condition = CompositeCondition.notOr(QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "NOT (" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(age <= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "OR " +
                "(create_time > #{ew.paramNameValuePairs.MPGENVAL3})))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(18, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(CREATE_TIME_START, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNested() {
        // sub condition 1: name = mock1 or name = mock2
        CompositeCondition subCondition1 = CompositeCondition.or(
                CompareCondition.eq("name", "mock1"),
                CompareCondition.eq("name", "mock2")
        );

        // sub condition 2: create_time > 2020-01-01 and create_time < 2022-01-01
        CompositeCondition subCondition2 = CompositeCondition.and(
                CompareCondition.gt("create_time", LocalDateTime.of(2020, 1, 1, 0, 0, 0)),
                CompareCondition.lt("create_time", LocalDateTime.of(2022, 1, 1, 0, 0, 0))
        );

        // sub condition 3: create_user <> 1
        CompareCondition subCondition3 = CompareCondition.ne("create_user", 1);

        // condition: (name = mock1 or name = mock2) and create_time > 2020-01-01 and create_time < 2022-01-01 and create_user <> 1
        CompositeCondition condition = CompositeCondition.and(subCondition1, subCondition2, subCondition3);

        // apply to QueryWrapper
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(name = #{ew.paramNameValuePairs.MPGENVAL2})" +
                ") " +
                "AND " +
                "(" +
                "(create_time > #{ew.paramNameValuePairs.MPGENVAL3}) " +
                "AND " +
                "(create_time < #{ew.paramNameValuePairs.MPGENVAL4})" +
                ") " +
                "AND " +
                "(create_user <> #{ew.paramNameValuePairs.MPGENVAL5}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock1", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals("mock2", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(CREATE_TIME_START, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
        assertEquals(CREATE_TIME_END, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL4"));
        assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL5"));
    }
}
