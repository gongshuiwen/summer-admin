package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.summernova.admin.core.field.Many2One;

import java.io.IOException;

/**
 * @author gongshuiwen
 */
public class Many2OneDeserializer extends StdDeserializer<Many2One<?>> {

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
