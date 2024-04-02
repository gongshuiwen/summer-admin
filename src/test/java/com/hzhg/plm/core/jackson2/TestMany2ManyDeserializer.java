package com.hzhg.plm.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hzhg.plm.core.entity.Mock1;
import com.hzhg.plm.core.entity.Mock2;
import com.hzhg.plm.core.fields.Command;
import com.hzhg.plm.core.fields.CommandType;
import com.hzhg.plm.core.fields.Many2Many;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMany2ManyDeserializer {

    ObjectMapper mapper;
    {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Many2Many.class, new Many2ManyDeserializer(Many2Many.class));
        mapper.registerModule(module);
    }

    @Test
    public void testCommandAdd() throws JsonProcessingException {
        Many2Many<?> many2Many = mapper.readValue("[[0, [1, 2, 3]]]", Many2Many.class);
        assertEquals(1, many2Many.getCommands().size());

        Command<?> command = many2Many.getCommands().get(0);
        assertEquals(CommandType.ADD, command.getCommandType());
        assertEquals(List.of(1L, 2L, 3L), command.getIds());
        assertNull(command.getEntities());
    }

    @Test
    public void testCommandCreate() throws JsonProcessingException {
        Mock1 mock1 = mapper.readValue("{\"mock2s\": [[1, {\"name\": \"mock\"}]]}", Mock1.class);
        Many2Many<Mock2> many2Many = mock1.getMock2s();
        assertEquals(1, many2Many.getCommands().size());

        Command<Mock2> command = many2Many.getCommands().get(0);
        assertEquals(CommandType.CREATE, command.getCommandType());
        assertNull(command.getIds());
        assertEquals(1, command.getEntities().size());
        assertEquals("mock", command.getEntities().get(0).getName());
    }

    @Test
    public void testCommandRemove() throws JsonProcessingException {
        Many2Many<?> many2Many = mapper.readValue("[[2, [1, 2, 3]]]", Many2Many.class);
        assertEquals(1, many2Many.getCommands().size());

        Command<?> command = many2Many.getCommands().get(0);
        assertEquals(CommandType.REMOVE, command.getCommandType());
        assertEquals(List.of(1L, 2L, 3L), command.getIds());
        assertNull(command.getEntities());
    }

    @Test
    public void testCommandRemoveAll() throws JsonProcessingException {
        Many2Many<?> many2Many = mapper.readValue("[[3]]", Many2Many.class);
        assertEquals(1, many2Many.getCommands().size());

        Command<?> command = many2Many.getCommands().get(0);
        assertEquals(CommandType.REMOVE_ALL, command.getCommandType());
        assertNull(command.getIds());
        assertNull(command.getEntities());
    }

    @Test
    public void testCommandReplace() throws JsonProcessingException {
        Many2Many<?> many2Many = mapper.readValue("[[6, [1, 2, 3]]]", Many2Many.class);
        assertEquals(1, many2Many.getCommands().size());

        Command<?> command = many2Many.getCommands().get(0);
        assertEquals(CommandType.REPLACE, command.getCommandType());
        assertEquals(List.of(1L, 2L, 3L), command.getIds());
        assertNull(command.getEntities());
    }
}
