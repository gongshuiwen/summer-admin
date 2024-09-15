package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.summernova.admin.common.query.CompositeCondition;
import io.summernova.admin.common.query.Condition;
import io.summernova.admin.common.query.SimpleCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestConditionDeserializer {

    ObjectMapper mapper;
    {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Condition.class, new ConditionDeserializer(Condition.class));
        mapper.registerModule(module);
    }

    @Test
    void testSimpleCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"=\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(SimpleCondition.class, condition);
        SimpleCondition simpleCondition = (SimpleCondition) condition;
        assertEquals("id", simpleCondition.getField());
        assertEquals("=", simpleCondition.getOperator());
        assertEquals("100", simpleCondition.getValue());
    }

    @Test
    void testCompositeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{" +
            "\"operator\":\"or\"," +
            "\"conditions\":[" +
                "{\"field\":\"id\",\"operator\":\"=\",\"value\":\"100\"}," +
                "{\"field\":\"id\",\"operator\":\"=\",\"value\":\"101\"}" +
            "]" +
        "}", Condition.class);

        assertInstanceOf(CompositeCondition.class, condition);
        CompositeCondition compositeCondition = (CompositeCondition) condition;
        assertEquals("or", compositeCondition.getOperator());
        assertEquals(2, compositeCondition.getConditions().length);

        assertInstanceOf(SimpleCondition.class, compositeCondition.getConditions()[0]);
        SimpleCondition condition1 = (SimpleCondition) compositeCondition.getConditions()[0];
        assertEquals("id", condition1.getField());
        assertEquals("=", condition1.getOperator());
        assertEquals("100", condition1.getValue());

        assertInstanceOf(SimpleCondition.class, compositeCondition.getConditions()[1]);
        SimpleCondition condition2 = (SimpleCondition) compositeCondition.getConditions()[1];
        assertEquals("id", condition2.getField());
        assertEquals("=", condition2.getOperator());
        assertEquals("101", condition2.getValue());
    }
}
