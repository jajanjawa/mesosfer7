package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.data.User;
import com.github.jajanjawa.mesosfer7.request.UserBody;
import com.github.jajanjawa.mesosfer7.response.LoginResponse;
import com.github.jajanjawa.mesosfer7.response.RegisterResponse;
import com.github.jajanjawa.mesosfer7.response.UpdateResponse;
import com.github.jajanjawa.mesosfer7.response.UserAttributes;
import com.github.jajanjawa.mesosfer7.util.RequestCallback;
import com.google.gson.JsonObject;
import okhttp3.RequestBody;

public class MesosferUser {

    private final MesosferRider rider;
    private String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MesosferUser(Application application) {
        rider = new MesosferRider(application);
    }

    public RegisterResponse register(UserBody user) throws MesosferException {
        return rider.path("users").post(constructUserBody(user)).execute().fromResultAs(RegisterResponse.class);
    }

    public MesosferCall register(UserBody user, MesosferCallback<RegisterResponse> callback) {
       return rider.path("users").post(constructUserBody(user)).enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                RegisterResponse registerResponse = response != null ? response.fromResultAs(RegisterResponse.class) : null;
                callback.handle(registerResponse, null);
            }
        });
    }

    private RequestBody constructUserBody(UserBody user) {
        return RequestBody.create(MesosferRider.JSON_MIME, Mesosfer.getGson().toJson(user));
    }

    public void deleteUserById(String id) throws MesosferException {
        rider.path("users/" + id).authorize(token).delete().execute();
    }

    public MesosferCall deleteUserById(String id, MesosferCallback<Void> callback) {
       return rider.path("users/" + id).authorize(token).delete().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                callback.handle(null, e);
            }
        });
    }

    public LoginResponse login(String email, String password) throws MesosferException {
        return rider.path("users/signin").post(constructLoginBody(email, password)).execute().as(LoginResponse.class);
    }

    public MesosferCall login(String email, String password, MesosferCallback<LoginResponse> callback) {
       return rider.path("users/signin").post(constructLoginBody(email, password)).enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                LoginResponse loginResponse = response != null ? response.as(LoginResponse.class) : null;
                callback.handle(loginResponse, e);
            }
        });
    }

    private RequestBody constructLoginBody(String email, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("email", email);
        data.addProperty("password", password);

        return RequestBody.create(MesosferRider.JSON_MIME, data.toString());
    }

    public User getUserById(String id) throws MesosferException {
        return rider.path("users/" + id).authorize(token).get().execute().fromResultAs(User.class);
    }

    public MesosferCall getUserById(String id, MesosferCallback<User> callback) {
        return rider.path("users/" + id).authorize(token).get().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                User user = response != null ? response.fromResultAs(User.class) : null;
                callback.handle(user, e);
            }
        });
    }

    public UserAttributes getUser() throws MesosferException {
        return rider.path("users/me").authorize(token).get().execute().fromResultAs(UserAttributes.class);
    }

    public MesosferCall getUser(MesosferCallback<UserAttributes> callback) {
       return rider.path("users/me").authorize(token).get().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                UserAttributes userAttributes = response != null ? response.fromResultAs(UserAttributes.class) : null;
                callback.handle(userAttributes, e);
            }
        });
    }

    /**
     * simpan pengguna.
     * untuk ganti kolom metadata harus diisi semua.
     *
     * @param id   objek id
     * @param user kolom yang perlu diganti
     * @return UpdateResponse
     * @throws MesosferException terjadi kesalahan
     */
    public UpdateResponse save(String id, UserBody user) throws MesosferException {
        return rider.path("users/" + id).authorize(token).put(constructUserBody(user)).execute().fromResultAs(UpdateResponse.class);
    }

    public MesosferCall save(String id, UserBody body, MesosferCallback<UpdateResponse> callback) {
       return rider.path("users/" + id).authorize(token).put(constructUserBody(body)).enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                UpdateResponse updateResponse = response != null ? response.fromResultAs(UpdateResponse.class) : null;
                callback.handle(updateResponse, e);
            }
        });
    }
}
