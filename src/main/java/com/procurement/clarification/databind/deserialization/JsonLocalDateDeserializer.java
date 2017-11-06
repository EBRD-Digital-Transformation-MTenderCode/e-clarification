package com.procurement.clarification.databind.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class JsonLocalDateDeserializer extends StdDeserializer<LocalDateTime> {

    private DateTimeFormatter formatter
        = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(ISO_LOCAL_DATE)
        .appendLiteral('T')
        .append(ISO_LOCAL_TIME)
        .appendLiteral('Z')
        .toFormatter();

    public JsonLocalDateDeserializer() {
        this(null);
    }

    public JsonLocalDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException,
        JsonProcessingException {
        String date = jsonParser.getText();
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
