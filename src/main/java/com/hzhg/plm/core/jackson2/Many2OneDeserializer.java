package com.hzhg.plm.core.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hzhg.plm.core.fields.Many2One;

import java.io.IOException;

public class Many2OneDeserializer extends StdDeserializer<Many2One<?>>{

    public Many2OneDeserializer() {
        this(null);
    }

    protected Many2OneDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Many2One<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Many2One.ofId(p.getCodec().readValue(p, Long.class));
    }
}
