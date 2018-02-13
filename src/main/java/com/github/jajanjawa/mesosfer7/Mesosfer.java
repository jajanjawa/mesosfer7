package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.util.DateAdapter;
import com.github.jajanjawa.mesosfer7.util.DateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;

import java.util.Date;

public class Mesosfer {

    private static Gson gson;
    private static OkHttpClient httpClient;
    private static String token;
    private static Application application;

    static {
        gson = new GsonBuilder()
                .setDateFormat(DateFormat.PATTERN)
                .registerTypeAdapter(Date.class, new DateAdapter())
                .create();
    }

    private Mesosfer() {
    }

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        Mesosfer.application = application;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Mesosfer.token = token;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        Mesosfer.gson = gson;
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(OkHttpClient httpClient) {
        Mesosfer.httpClient = httpClient;
    }

}
