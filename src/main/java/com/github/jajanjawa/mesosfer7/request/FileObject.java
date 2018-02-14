package com.github.jajanjawa.mesosfer7.request;

import com.github.jajanjawa.mesosfer7.Mesosfer;
import com.github.jajanjawa.mesosfer7.MesosferCallback;
import com.github.jajanjawa.mesosfer7.MesosferException;
import com.google.gson.annotations.SerializedName;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * untuk kolom dengan tipe file
 */
public class FileObject {
    private String name;
    private String url;
    @SerializedName("__type")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }


    public void download(MesosferCallback<byte[]> callback) {
        Request request = new Request.Builder().url(url).get().build();
        Mesosfer.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.handle(null, new MesosferException(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BufferedSink sink = Okio.buffer(Okio.sink(outputStream));
                sink.writeAll(response.body().source());
                sink.close();
                callback.handle(outputStream.toByteArray(), null);
            }
        });
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
