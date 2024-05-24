package com.hzboiler.core.jackson2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hzboiler.core.entity.BaseEntity;
import com.hzboiler.core.fields.Command;
import com.hzboiler.core.fields.CommandType;
import com.hzboiler.core.fields.One2Many;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongshuiwen
 */
public class One2ManyDeserializer extends StdDeserializer<One2Many<?>> implements ContextualDeserializer {

    private JavaType type;
    private final TypeReference<List<Long>> typeReferenceListLong = new TypeReference<>() {
    };

    public One2ManyDeserializer() {
        this(null);
    }

    protected One2ManyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            // TODO: is this thread safe?
            this.type = property.getType().containedType(0);
        }
        return this;
    }

    @Override
    public One2Many<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonParseException {
        JsonNode rootNode = p.getCodec().readTree(p);
        if (rootNode instanceof ArrayNode arrayNode) {
            List<Command<BaseEntity>> commands = new ArrayList<>();
            for (JsonNode subNode : arrayNode) {
                if (subNode instanceof ArrayNode commandNode) {
                    CommandType type = CommandType.of(commandNode.get(0).asInt());
                    try {
                        switch (type) {
                            case CREATE: {
                                List<BaseEntity> objects = new ArrayList<>();
                                for (JsonNode jsonNode : commandNode.get(1))
                                    objects.add(p.getCodec().readValue(jsonNode.traverse(), this.type));
                                commands.add(Command.create(objects));
                                break;
                            }
                            case DELETE: {
                                commands.add(Command.delete(p.getCodec().readValue(commandNode.get(1).traverse(), typeReferenceListLong)));
                                break;
                            }
                            case UPDATE: {
                                commands.add(Command.update(
                                        p.getCodec().readValue(commandNode.get(1).traverse(), Long.class),
                                        p.getCodec().readValue(commandNode.get(2).traverse(), this.type)));
                                break;
                            }
                        }
                    } catch (IOException e) {
                        throw new JsonParseException(p, "Not a valid One2Many: " + rootNode);
                    }
                }
            }

            if (!commands.isEmpty()) {
                return One2Many.ofCommands(commands);
            }
        }

        throw new JsonParseException(p, "Not a valid One2Many: " + rootNode);
    }
}
