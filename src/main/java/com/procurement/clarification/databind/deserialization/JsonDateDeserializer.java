package com.procurement.clarification.databind.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JsonDateDeserializer extends StdDeserializer<Date> {

    private SimpleDateFormat formatter
        = new SimpleDateFormat("yyyy-MM-dd");

    public JsonDateDeserializer() {
        this(null);
    }

    public JsonDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException,
        JsonProcessingException {
        String date = jsonParser.getText();
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
