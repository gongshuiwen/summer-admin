package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.core.domain.field.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestCommandTypeMixin {

    ObjectMapper objectMapper = new ObjectMapper().addMixIn(CommandType.class, CommonTypeMixin.class);

    @Test
    void testSerialize() throws JsonProcessingException {
        assertEquals("0", objectMapper.writeValueAsString(CommandType.ADD));
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        assertEquals(CommandType.ADD, objectMapper.readValue("0", CommandType.class));
    }
}
