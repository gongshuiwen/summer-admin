package com.hzboiler.erp.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hzboiler.erp.core.field.One2Many;

import java.io.IOException;

/**
 * @author gongshuiwen
 */
public class One2ManySerializer extends StdSerializer<One2Many<?>> {

    public One2ManySerializer() {
        this(null);
    }

    protected One2ManySerializer(Class<One2Many<?>> t) {
        super(t);
    }

    @Override
    public void serialize(One2Many<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value.get());
    }
}
