package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.summernova.admin.core.domain.field.Command;
import io.summernova.admin.core.domain.field.CommandType;
import io.summernova.admin.core.domain.field.One2Many;
import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.core.domain.model.Mock2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author gongshuiwen
 */
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
        assertEquals(2, command.getRecords().size());
        assertEquals("mock2-1", command.getRecords().get(0).getName());
        assertEquals("mock2-2", command.getRecords().get(1).getName());
    }

    @Test
    void testCommandDelete() throws JsonProcessingException {
        One2Many<?> One2Many = mapper.readValue("[[4, [1, 2, 3]]]", One2Many.class);
        assertEquals(1, One2Many.getCommands().size());

        Command<?> command = One2Many.getCommands().get(0);
        assertEquals(CommandType.DELETE, command.getCommandType());
        assertEquals(List.of(1L, 2L, 3L), command.getIds());
        assertNull(command.getRecords());
    }

    @Test
    void testCommandUpdate() throws JsonProcessingException {
        Mock1 mock1 = mapper.readValue("{\"mock2s1\": [[5, 1, {\"name\": \"mock2-1\"}]]}", Mock1.class);
        One2Many<Mock2> one2Many = mock1.getMock2s1();
        assertEquals(1, one2Many.getCommands().size());

        Command<Mock2> command = one2Many.getCommands().get(0);
        assertEquals(CommandType.UPDATE, command.getCommandType());
        assertEquals(List.of(1L), command.getIds());
        assertEquals(1, command.getRecords().size());
        assertEquals("mock2-1", command.getRecords().get(0).getName());
    }
}
