package com.github.jajanjawa.mesosfer7.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return DateFormat.getInstance().parseServer(json.getAsString());
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        String time = DateFormat.getInstance().formatServer(src);
        return new JsonPrimitive(time);
    }
}
