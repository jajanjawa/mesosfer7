package com.github.jajanjawa.mesosfer7;

import okhttp3.*;

import java.net.MalformedURLException;
import java.net.URL;

public class MesosferRider {
    public static final MediaType JSON_MIME = MediaType.parse("application/json");
    private static final String AUTHORIZATION = "Authorization";
    private final Headers.Builder headersBuilder;
    private final Request.Builder requestBuilder;
    private URL server;
    private HttpUrl.Builder urlBuilder;

    public MesosferRider(String serverUrl) {
        server(serverUrl);

        headersBuilder = new Headers.Builder();
        requestBuilder = new Request.Builder();

        setContentType(JSON_MIME.toString());
    }

    public void setContentType(String type) {
        headersBuilder.set("Content-Type", type);
    }

    public MesosferRider(Application application) {
        this();
        application(application);
    }

    public MesosferRider() {
        this("https://api.mesosfer.com/api/v2/");
    }

    public HttpUrl.Builder getUrlBuilder() {
        return urlBuilder;
    }


    private void server(String url) {
        try {
            server = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Headers.Builder getHeadersBuilder() {
        return headersBuilder;
    }

    public MesosferCall get() {
        Request request = requestBuilder.headers(headersBuilder.build())
                .url(urlBuilder.build())
                .get().build();
        return new MesosferCall(request);
    }

    public Request.Builder getRequestBuilder() {
        return requestBuilder;
    }

    public MesosferCall delete() {
        Request request = requestBuilder.headers(headersBuilder.build())
                .url(urlBuilder.build())
                .delete().build();
        return new MesosferCall(request);
    }

    public MesosferRider addPathSegments(String pathSegments) {
        if (urlBuilder == null) {
            urlBuilder = HttpUrl.get(server).newBuilder();
        }
        urlBuilder.addPathSegments(pathSegments);
        return this;
    }

    public MesosferRider path(String path) {
        urlBuilder = HttpUrl.get(server).newBuilder();
        urlBuilder.addPathSegments(path);
        return this;
    }

    public MesosferRider application(Application application) {
        headersBuilder.set("X-Mesosfer-AppId", application.id);
        headersBuilder.set("X-Mesosfer-AppKey", application.key);
        return this;
    }

    public MesosferRider authorize(String token) {
        if (token.isEmpty()) {
            throw new IllegalStateException("token kosong");
        }
        headersBuilder.set(AUTHORIZATION, "Bearer " + token);
        return this;
    }

    public MesosferCall post(RequestBody body) {
        Request request = requestBuilder.headers(headersBuilder.build())
                .url(urlBuilder.build())
                .post(body)
                .build();
        return new MesosferCall(request);
    }

    public MesosferCall put(RequestBody body) {
        Request request = requestBuilder.headers(headersBuilder.build())
                .url(urlBuilder.build())
                .put(body)
                .build();
        return new MesosferCall(request);
    }
}
