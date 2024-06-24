package com.hzboiler.erp.core.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hzboiler.erp.core.protocal.query.CompositeCondition;
import com.hzboiler.erp.core.protocal.query.Condition;
import com.hzboiler.erp.core.protocal.query.SimpleCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConditionDeserializer extends StdDeserializer<Condition> {

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

        if (node.has("field")) {
            String field = node.get("field").asText();
            String operator = node.get("operator").asText();
            String value = node.get("value").asText();
            return SimpleCondition.of(field, operator, value);
        } else if (node.has("conditions")) {
            String operator = node.get("operator").asText();
            List<Condition> conditions = new ArrayList<>();
            for (JsonNode conditionNode : node.get("conditions")) {
                conditions.add(mapper.treeToValue(conditionNode, Condition.class));
            }
            return CompositeCondition.of(operator, conditions);
        }

        throw new IllegalArgumentException("Invalid JSON structure for Condition");
    }
}