package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.summernova.admin.common.query.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
    void testEqCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"=\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals("=", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testNeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"!=\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals("!=", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testLtCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"<\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals("<", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testGtCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\">\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals(">", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testLeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"<=\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals("<=", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testGeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\">=\",\"value\":\"100\"}", Condition.class);
        assertInstanceOf(GeneralCondition.class, condition);
        GeneralCondition generalCondition = (GeneralCondition) condition;
        assertEquals("id", generalCondition.getField());
        assertEquals(">=", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }

    @Test
    void testLikeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"like\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("like", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testLikeLeftCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"likeLeft\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("likeLeft", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testLikeRightCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"likeRight\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("likeRight", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testNotLikeCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"notLike\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("notLike", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testNotLikeLeftCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"notLikeLeft\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("notLikeLeft", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testNotLikeRightCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"name\",\"operator\":\"notLikeRight\",\"value\":\"mock\"}", Condition.class);
        assertInstanceOf(LikeCondition.class, condition);
        LikeCondition likeCondition = (LikeCondition) condition;
        assertEquals("name", likeCondition.getField());
        assertEquals("notLikeRight", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testBetweenCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"between\",\"value1\":\"100\",\"value2\":\"200\"}", Condition.class);
        assertInstanceOf(BetweenCondition.class, condition);
        BetweenCondition betweenCondition = (BetweenCondition) condition;
        assertEquals("id", betweenCondition.getField());
        assertEquals("between", betweenCondition.getOperator());
        assertEquals("100", betweenCondition.getValue1());
        assertEquals("200", betweenCondition.getValue2());
    }

    @Test
    void testNotBetweenCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"notBetween\",\"value1\":\"100\",\"value2\":\"200\"}", Condition.class);
        assertInstanceOf(BetweenCondition.class, condition);
        BetweenCondition betweenCondition = (BetweenCondition) condition;
        assertEquals("id", betweenCondition.getField());
        assertEquals("notBetween", betweenCondition.getOperator());
        assertEquals("100", betweenCondition.getValue1());
        assertEquals("200", betweenCondition.getValue2());
    }

    @Test
    void testNullCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"isNull\"}", Condition.class);
        assertInstanceOf(NullCondition.class, condition);
        NullCondition nullCondition = (NullCondition) condition;
        assertEquals("id", nullCondition.getField());
        assertEquals("isNull", nullCondition.getOperator());
    }

    @Test
    void testNotNullCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"isNotNull\"}", Condition.class);
        assertInstanceOf(NullCondition.class, condition);
        NullCondition nullCondition = (NullCondition) condition;
        assertEquals("id", nullCondition.getField());
        assertEquals("isNotNull", nullCondition.getOperator());
    }

    @Test
    void testInCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"in\",\"values\":[\"100\",\"101\"]}", Condition.class);
        assertInstanceOf(InCondition.class, condition);
        InCondition inCondition = (InCondition) condition;
        assertEquals("id", inCondition.getField());
        assertEquals("in", inCondition.getOperator());
        assertEquals(2, inCondition.getValues().length);
        assertEquals("100", inCondition.getValues()[0]);
        assertEquals("101", inCondition.getValues()[1]);
    }

    @Test
    void testNotInCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{\"field\":\"id\",\"operator\":\"notIn\",\"values\":[\"100\",\"101\"]}", Condition.class);
        assertInstanceOf(InCondition.class, condition);
        InCondition inCondition = (InCondition) condition;
        assertEquals("id", inCondition.getField());
        assertEquals("notIn", inCondition.getOperator());
        assertEquals(2, inCondition.getValues().length);
        assertEquals("100", inCondition.getValues()[0]);
        assertEquals("101", inCondition.getValues()[1]);
    }

    @Test
    void testCompositeAndCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{" +
                "\"operator\":\"and\"," +
                "\"conditions\":[" +
                "{\"field\":\"id\",\"operator\":\"=\",\"value\":\"100\"}," +
                "{\"field\":\"name\",\"operator\":\"like\",\"value\":\"mock\"}" +
                "]" +
                "}", Condition.class);

        assertInstanceOf(CompositeCondition.class, condition);
        CompositeCondition compositeCondition = (CompositeCondition) condition;
        assertEquals("and", compositeCondition.getOperator());
        assertEquals(2, compositeCondition.getConditions().length);

        assertInstanceOf(GeneralCondition.class, compositeCondition.getConditions()[0]);
        GeneralCondition generalCondition1 = (GeneralCondition) compositeCondition.getConditions()[0];
        assertEquals("id", generalCondition1.getField());
        assertEquals("=", generalCondition1.getOperator());
        assertEquals("100", generalCondition1.getValue());

        assertInstanceOf(LikeCondition.class, compositeCondition.getConditions()[1]);
        LikeCondition likeCondition = (LikeCondition) compositeCondition.getConditions()[1];
        assertEquals("name", likeCondition.getField());
        assertEquals("like", likeCondition.getOperator());
        assertEquals("mock", likeCondition.getValue());
    }

    @Test
    void testCompositeOrCondition() throws JsonProcessingException {
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

        assertInstanceOf(GeneralCondition.class, compositeCondition.getConditions()[0]);
        GeneralCondition generalCondition1 = (GeneralCondition) compositeCondition.getConditions()[0];
        assertEquals("id", generalCondition1.getField());
        assertEquals("=", generalCondition1.getOperator());
        assertEquals("100", generalCondition1.getValue());

        assertInstanceOf(GeneralCondition.class, compositeCondition.getConditions()[1]);
        GeneralCondition generalCondition2 = (GeneralCondition) compositeCondition.getConditions()[1];
        assertEquals("id", generalCondition2.getField());
        assertEquals("=", generalCondition2.getOperator());
        assertEquals("101", generalCondition2.getValue());
    }

    @Test
    void testCompositeNotCondition() throws JsonProcessingException {
        Condition condition = mapper.readValue("{" +
                "\"operator\":\"not\"," +
                "\"conditions\":[" +
                "{\"field\":\"id\",\"operator\":\"=\",\"value\":\"100\"}" +
                "]" +
                "}", Condition.class);

        assertInstanceOf(CompositeCondition.class, condition);
        CompositeCondition compositeCondition = (CompositeCondition) condition;
        assertEquals("not", compositeCondition.getOperator());

        assertInstanceOf(GeneralCondition.class, compositeCondition.getConditions()[0]);
        GeneralCondition generalCondition = (GeneralCondition) compositeCondition.getConditions()[0];
        assertEquals("id", generalCondition.getField());
        assertEquals("=", generalCondition.getOperator());
        assertEquals("100", generalCondition.getValue());
    }
}
