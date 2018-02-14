package com.github.jajanjawa.mesosfer7.request;

import com.github.jajanjawa.mesosfer7.Mesosfer;
import com.github.jajanjawa.mesosfer7.data.User;
import com.github.jajanjawa.mesosfer7.util.JsonUtil;
import com.google.gson.JsonObject;

public class UserBody {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String country;
    public String phone;
    private JsonObject metadata;

    public UserBody() {
        metadata = new JsonObject();
    }

    /**
     * @param user sandi tidak disalin
     * @return UserBody
     */
    public UserBody copy(User user) {
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();
        country = user.getCountry();
        phone = user.getPhone();
        metadata = user.getMetadata();

        return this;
    }

    /**
     * kolom tambahan dalam metadata.
     * @param name nama kolom
     * @param value isi
     * @return UserBody
     */
    public UserBody field(String name, Object value) {
        JsonUtil.addProperty(metadata, name, value);
        return this;
    }

    public void metadata(Object data) {
        metadata = Mesosfer.getGson().toJsonTree(data).getAsJsonObject();
    }
}
