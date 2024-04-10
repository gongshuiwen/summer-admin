package com.hzhg.plm.core.jackson2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.fields.Command;
import com.hzhg.plm.core.fields.CommandType;
import com.hzhg.plm.core.fields.Many2Many;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Many2ManyDeserializer extends StdDeserializer<Many2Many<?>> {

    private final TypeReference<List<Long>> typeReferenceListLong = new TypeReference<>() {
    };

    public Many2ManyDeserializer() {
        this(null);
    }

    protected Many2ManyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Many2Many<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        if (rootNode instanceof ArrayNode arrayNode) {
            List<Command<BaseEntity>> commands = new ArrayList<>();
            for (JsonNode subNode : arrayNode) {
                if (subNode instanceof ArrayNode commandNode) {
                    CommandType type = CommandType.of(commandNode.get(0).asInt());
                    try {
                        switch (type) {
                            case ADD: {
                                commands.add(Command.add(p.getCodec().readValue(commandNode.get(1).traverse(), typeReferenceListLong)));
                                break;
                            }
                            case REMOVE: {
                                commands.add(Command.remove(p.getCodec().readValue(commandNode.get(1).traverse(), typeReferenceListLong)));
                                break;
                            }
                            case REPLACE: {
                                commands.add(Command.replace(p.getCodec().readValue(commandNode.get(1).traverse(), typeReferenceListLong)));
                                break;
                            }
                        }
                    } catch (IOException e) {
                        throw new JsonParseException(p, "Not a valid Many2Many: " + rootNode);
                    }
                }
            }

            if (!commands.isEmpty()) {
                return Many2Many.ofCommands(commands);
            }
        }

        throw new JsonParseException(p, "Not a valid Many2Many: " + rootNode);
    }
}
