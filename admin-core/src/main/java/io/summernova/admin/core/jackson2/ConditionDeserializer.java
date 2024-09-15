package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.summernova.admin.common.query.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConditionDeserializer extends StdDeserializer<Condition> {

    private static final String FIELD = "field";
    private static final String OPERATOR = "operator";
    private static final String VALUE = "value";
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    private static final String VALUES = "values";
    private static final String CONDITIONS = "conditions";

    protected ConditionDeserializer(Class<?> vc) {
        super(vc);
    }

    public ConditionDeserializer() {
        this(null);
    }

    @Override
    public Condition deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);
        String operator = node.get(OPERATOR).asText();
        if (operator == null)
            throw new IllegalArgumentException("Invalid JSON structure for Condition, operator is missing.");

        if (GeneralOperator.contains(operator)) {
            String field = node.get(FIELD).asText();
            String value = node.get(VALUE).asText();
            return GeneralCondition.of(field, operator, value);
        } else if (LikeOperator.contains(operator)) {
            String field = node.get(FIELD).asText();
            String value = node.get(VALUE).asText();
            return LikeCondition.of(field, operator, value);
        } else if (BetweenCondition.OPERATOR_BETWEEN.equals(operator)) {
            String field = node.get(FIELD).asText();
            String value1 = node.get(VALUE1).asText();
            String value2 = node.get(VALUE2).asText();
            return BetweenCondition.between(field, value1, value2);
        } else if (BetweenCondition.OPERATOR_NOT_BETWEEN.equals(operator)) {
            String field = node.get(FIELD).asText();
            String value1 = node.get(VALUE1).asText();
            String value2 = node.get(VALUE2).asText();
            return BetweenCondition.notBetween(field, value1, value2);
        } else if (NullCondition.OPERATOR_IS_NULL.equals(operator)) {
            String field = node.get(FIELD).asText();
            return NullCondition.isNull(field);
        } else if (NullCondition.OPERATOR_IS_NOT_NULL.equals(operator)) {
            String field = node.get(FIELD).asText();
            return NullCondition.isNotNull(field);
        } else if (InCondition.OPERATOR_IN.equals(operator)) {
            String field = node.get(FIELD).asText();
            List<String> values = new ArrayList<>();
            for (JsonNode valueNode : node.get(VALUES))
                values.add(valueNode.asText());
            return InCondition.in(field, values);
        } else if (InCondition.OPERATOR_NOT_IN.equals(operator)) {
            String field = node.get(FIELD).asText();
            List<Object> values = new ArrayList<>();
            for (JsonNode valueNode : node.get(VALUES))
                values.add(valueNode.asText());
            return InCondition.notIn(field, values);
        } else if (CompositeOperator.contains(operator)) {
            List<Condition> conditions = new ArrayList<>();
            for (JsonNode conditionNode : node.get(CONDITIONS))
                conditions.add(mapper.treeToValue(conditionNode, Condition.class));
            return CompositeCondition.of(operator, conditions);
        } else {
            throw new IllegalArgumentException("Invalid JSON structure for Condition, invalid operator: " + operator);
        }
    }
}