package com.github.jajanjawa.mesosfer7;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public class MesosferResponse {
    private String raw;

    public MesosferResponse(Response response) throws MesosferException {
        try {
            raw = response.body().string();
        } catch (IOException e) {
            throw new MesosferException(e);
        }

        if (!response.isSuccessful()) {
            throw new MesosferException(error());
        }
    }

    public String getRaw() {
        return raw;
    }

    public <T> T as(Type type) {
        return Mesosfer.getGson().fromJson(raw, type);
    }

    public <T> T fromResultAs(Type type) {
        return Mesosfer.getGson().fromJson(result(), type);
    }

    public JsonObject error() {
        JsonObject object = Mesosfer.getGson().fromJson(raw, JsonObject.class);
        return object.get("error").getAsJsonObject();
    }

    public long count() {
        JsonObject object = Mesosfer.getGson().fromJson(raw, JsonObject.class);
        return object.get("count").getAsLong();
    }

    public JsonObject result() {
        JsonObject object = Mesosfer.getGson().fromJson(raw, JsonObject.class);
        return object.get("result").getAsJsonObject();
    }

    public JsonArray resultArray() {
        JsonObject object = Mesosfer.getGson().fromJson(raw, JsonObject.class);
        return object.get("results").getAsJsonArray();
    }
}
