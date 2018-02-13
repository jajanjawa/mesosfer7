package com.github.jajanjawa.mesosfer7.data;

import com.github.jajanjawa.mesosfer7.Mesosfer;
import com.github.jajanjawa.mesosfer7.MesosferObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class User extends MesosferObject {

    private String country;
    @SerializedName("validemail")
    private String email;
    private String firstname;
    private String lastname;
    private JsonObject metadata;
    private String phone;

    public JsonElement getField(String name) {
        return metadata.get(name);
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public <T> T convertMetadata(Class<T> clazz) {
        return Mesosfer.getGson().fromJson(metadata, clazz);
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }
}
