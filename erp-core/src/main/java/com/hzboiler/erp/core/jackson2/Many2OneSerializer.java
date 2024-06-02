package com.hzboiler.erp.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.hzboiler.erp.core.field.Many2One;

import java.io.IOException;

/**
 * @author gongshuiwen
 */
public class Many2OneSerializer extends StdSerializer<Many2One<?>> {

    public Many2OneSerializer() {
        this(null);
    }

    protected Many2OneSerializer(Class<Many2One<?>> t) {
        super(t);
    }

    @Override
    public void serialize(Many2One<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value.getRecord());
    }
}
