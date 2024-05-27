package com.hzboiler.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hzboiler.core.field.Many2Many;

import java.io.IOException;

/**
 * @author gongshuiwen
 */
public class Many2ManySerializer extends StdSerializer<Many2Many<?>> {

    public Many2ManySerializer() {
        this(null);
    }

    protected Many2ManySerializer(Class<Many2Many<?>> t) {
        super(t);
    }

    @Override
    public void serialize(Many2Many<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value.get());
    }
}
