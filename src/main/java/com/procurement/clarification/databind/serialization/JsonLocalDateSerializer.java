package com.procurement.clarification.databind.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class JsonLocalDateSerializer extends StdSerializer<LocalDateTime> {

    private  DateTimeFormatter formatter
        = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(ISO_LOCAL_DATE)
        .appendLiteral('T')
        .append(ISO_LOCAL_TIME)
        .appendLiteral('Z')
        .toFormatter();

    public JsonLocalDateSerializer() {
        this(null);
    }

    public JsonLocalDateSerializer(Class<LocalDateTime> t) {
        super (t);
    }

    @Override
    public void serialize(LocalDateTime localDateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatter.format(localDateTime));

    }
}