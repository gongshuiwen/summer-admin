package com.hzboiler.erp.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hzboiler.erp.core.jackson2.One2ManyDeserializer;
import com.hzboiler.erp.core.model.Mock1;
import com.hzboiler.erp.core.model.Mock2;
import com.hzboiler.erp.core.field.Command;
import com.hzboiler.erp.core.field.CommandType;
import com.hzboiler.erp.core.field.One2Many;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestOne2ManyDeserializer {

    ObjectMapper mapper;
    {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(One2Many.class, new One2ManyDeserializer(One2Many.class));
        mapper.registerModule(module);
    }

    @Test
    void testCommandCreate() throws JsonProcessingException {
        Mock1 mock1 = mapper.readValue("{\"mock2s1\": [[3, [{\"name\": \"mock2-1\"}, {\"name\": \"mock2-2\"}]]]}", Mock1.class);
        One2Many<Mock2> one2Many = mock1.getMock2s1();
        assertEquals(1, one2Many.getCommands().size());

        Command<Mock2> command = one2Many.getCommands().get(0);
        assertEquals(CommandType.CREATE, command.getCommandType());
        assertNull(command.getIds());
        assertEquals(2, command.getEntities().size());
        assertEquals("mock2-1", command.getEntities().get(0).getName());
        assertEquals("mock2-2", command.getEntities().get(1).getName());
    }

    @Test
    void testCommandDelete() throws JsonProcessingException {
        One2Many<?> One2Many = mapper.readValue("[[4, [1, 2, 3]]]", One2Many.class);
        assertEquals(1, One2Many.getCommands().size());

        Command<?> command = One2Many.getCommands().get(0);
        assertEquals(CommandType.DELETE, command.getCommandType());
        assertEquals(List.of(1L, 2L, 3L), command.getIds());
        assertNull(command.getEntities());
    }

    @Test
    void testCommandUpdate() throws JsonProcessingException {
        Mock1 mock1 = mapper.readValue("{\"mock2s1\": [[5, 1, {\"name\": \"mock2-1\"}]]}", Mock1.class);
        One2Many<Mock2> one2Many = mock1.getMock2s1();
        assertEquals(1, one2Many.getCommands().size());

        Command<Mock2> command = one2Many.getCommands().get(0);
        assertEquals(CommandType.UPDATE, command.getCommandType());
        assertEquals(List.of(1L), command.getIds());
        assertEquals(1, command.getEntities().size());
        assertEquals("mock2-1", command.getEntities().get(0).getName());
    }
}
