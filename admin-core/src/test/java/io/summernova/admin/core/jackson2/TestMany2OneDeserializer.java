package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.summernova.admin.core.field.Many2One;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author gongshuiwen
 */
class TestMany2OneDeserializer {

    ObjectMapper mapper;
    {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Many2One.class, new Many2OneDeserializer(Many2One.class));
        mapper.registerModule(module);
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        Many2One<?> many2One = mapper.readValue("1", Many2One.class);
        assertEquals(1, many2One.getId());
    }
}
