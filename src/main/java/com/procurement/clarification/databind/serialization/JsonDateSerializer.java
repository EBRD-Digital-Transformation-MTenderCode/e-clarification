package com.procurement.clarification.databind.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer extends StdSerializer<Date> {

    private SimpleDateFormat formatter
        = new SimpleDateFormat("yyyy-MM-dd");

    public JsonDateSerializer() {
        this(null);
    }

    public JsonDateSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(Date date,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatter.format(date));
    }
}