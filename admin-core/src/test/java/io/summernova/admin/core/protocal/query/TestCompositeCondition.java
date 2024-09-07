package io.summernova.admin.core.protocal.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.Mock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestCompositeCondition {

    static final LocalDateTime CREATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    static final List<Condition> SIMPLE_QUERY_CONDITIONS = Arrays.asList(
            SimpleCondition.of("name", "=", "mock"),
            SimpleCondition.of("create_time", ">=", CREATE_TIME),
            SimpleCondition.of("create_user", "=", 1)
    );

    @Test
    void testApplyToQueryWrapperCompositeAnd() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.and(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
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
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.or(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
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
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.not(SimpleCondition.of("name", "=", "mock"));
        condition.applyToQueryWrapper(mockQueryWrapper);
        assertEquals("(NOT (name = #{ew.paramNameValuePairs.MPGENVAL1}))", mockQueryWrapper.getSqlSegment());
        assertEquals("mock", mockQueryWrapper.getParamNameValuePairs().get("MPGENVAL1"));
    }

    @Test
    void testApplyToQueryWrapperCompositeNotAnd() {
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.notAnd(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
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
        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        Condition condition = CompositeCondition.notOr(SIMPLE_QUERY_CONDITIONS);
        condition.applyToQueryWrapper(mockQueryWrapper);
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

        Condition condition = CompositeCondition.and(List.of(subCondition1, subCondition2, subCondition3));

        QueryWrapper<Mock> mockQueryWrapper = new QueryWrapper<>();
        condition.applyToQueryWrapper(mockQueryWrapper);
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

    @Test
    void testGetSql() {
        String sql = "( " +
                "( name = 'mock1' OR name = 'mock2' ) " +
                "AND ( create_time > '2020-01-01 00:00:00' AND create_time < '2024-01-01 00:00:00' ) " +
                "AND NOT ( create_user = 1 OR create_user = 2 ) " +
                "AND NOT ( id = 1 AND create_user = 3 ) " +
                "AND NOT is_deleted = true )";

        SimpleCondition simpleCondition1 = SimpleCondition.of("name", "=", "mock1");
        SimpleCondition simpleCondition2 = SimpleCondition.of("name", "=", "mock2");
        CompositeCondition compositeCondition1 = CompositeCondition.or(List.of(simpleCondition1, simpleCondition2));

        SimpleCondition simpleCondition3 = SimpleCondition.of("create_time", ">", "2020-01-01 00:00:00");
        SimpleCondition simpleCondition4 = SimpleCondition.of("create_time", "<", "2024-01-01 00:00:00");
        CompositeCondition compositeCondition2 = CompositeCondition.and(List.of(simpleCondition3, simpleCondition4));

        SimpleCondition simpleCondition5 = SimpleCondition.of("create_user", "=", 1);
        SimpleCondition simpleCondition6 = SimpleCondition.of("create_user", "=", 2);
        CompositeCondition compositeCondition3 = CompositeCondition.notOr(List.of(simpleCondition5, simpleCondition6));

        SimpleCondition simpleCondition7 = SimpleCondition.of("id", "=", 1);
        SimpleCondition simpleCondition8 = SimpleCondition.of("create_user", "=", 3);
        CompositeCondition compositeCondition4 = CompositeCondition.notAnd(List.of(simpleCondition7, simpleCondition8));

        SimpleCondition simpleCondition9 = SimpleCondition.of("is_deleted", "=", true);
        CompositeCondition compositeCondition5 = CompositeCondition.not(simpleCondition9);

        CompositeCondition compositeCondition6 = CompositeCondition.and(List.of(compositeCondition1, compositeCondition2, compositeCondition3, compositeCondition4, compositeCondition5));

        assertEquals(sql, compositeCondition6.getSql());
    }
}