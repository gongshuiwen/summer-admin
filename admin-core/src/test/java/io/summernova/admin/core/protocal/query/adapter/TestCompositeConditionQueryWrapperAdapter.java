package io.summernova.admin.core.protocal.query.adapter;

import io.summernova.admin.core.protocal.query.CompositeCondition;
import io.summernova.admin.core.protocal.query.Condition;
import io.summernova.admin.core.protocal.query.SimpleCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompositeConditionQueryWrapperAdapter extends QueryWrapperAdapterTestBase<CompositeConditionQueryWrapperAdapter> {

    static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    static final List<Condition> SIMPLE_QUERY_CONDITIONS = Arrays.asList(
            SimpleCondition.of("name", "=", "mock"),
            SimpleCondition.of("create_time", ">=", CREATE_TIME),
            SimpleCondition.of("create_user", "=", 1)
    );

    @Test
    void testApplyToQueryWrapperCompositeAnd() {
        CompositeCondition condition = CompositeCondition.and(SIMPLE_QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "(create_time >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "(create_user = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeOr() {
        CompositeCondition condition = CompositeCondition.or(SIMPLE_QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(create_time >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "OR " +
                "(create_user = #{ew.paramNameValuePairs.MPGENVAL3}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNot() {
        CompositeCondition condition = CompositeCondition.not(SimpleCondition.of("name", "=", "mock"));
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNotAnd() {
        CompositeCondition condition = CompositeCondition.notAnd(SIMPLE_QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "NOT (" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "AND " +
                "(create_time >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "AND " +
                "(create_user = #{ew.paramNameValuePairs.MPGENVAL3})))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNotOr() {
        CompositeCondition condition = CompositeCondition.notOr(SIMPLE_QUERY_CONDITIONS);
        queryWrapperAdapter.applyToQueryWrapper(condition, mockQueryWrapper);
        assertEquals("(" +
                "NOT (" +
                "(name = #{ew.paramNameValuePairs.MPGENVAL1}) " +
                "OR " +
                "(create_time >= #{ew.paramNameValuePairs.MPGENVAL2}) " +
                "OR " +
                "(create_user = #{ew.paramNameValuePairs.MPGENVAL3})))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
        assertEquals(CREATE_TIME, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL2"));
        assertEquals(1, mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL3"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNested() {
        CompositeCondition subCondition1 = CompositeCondition.or(List.of(
                SimpleCondition.of("name", "=", "mock1"),
                SimpleCondition.of("name", "=", "mock2")
        ));

        CompositeCondition subCondition2 = CompositeCondition.and(List.of(
                SimpleCondition.of("create_time", ">", LocalDateTime.of(2020, 1, 1, 0, 0, 0)),
                SimpleCondition.of("create_time", "<", LocalDateTime.of(2022, 1, 1, 0, 0, 0))
        ));

        SimpleCondition subCondition3 = SimpleCondition.of("create_user", "!=", 1);

        CompositeCondition condition = CompositeCondition.and(List.of(subCondition1, subCondition2, subCondition3));

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
    }
}
