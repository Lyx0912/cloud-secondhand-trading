package com.lyx.common.base.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @Author: xhj
 * @Date: 2023/04/20/20:24
 * @Description:
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateTimeStr = jsonParser.getText().trim();
        try {
            return LocalDateTime.parse(dateTimeStr, LOCAL_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse LocalDateTime: "+dateTimeStr, e);
        }
    }
}