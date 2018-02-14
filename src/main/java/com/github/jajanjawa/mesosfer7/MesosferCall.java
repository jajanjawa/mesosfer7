package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.util.RequestCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MesosferCall {

    private final Call call;

    public MesosferCall(Request request) {
        call = Mesosfer.getHttpClient().newCall(request);
    }

    public MesosferResponse execute() throws MesosferException {
        try {
            return new MesosferResponse(call.execute());
        } catch (IOException e) {
            throw new MesosferException(e);
        }
    }

    public Request request() {
        return call.request();
    }

    public MesosferCall enqueue(RequestCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.handle(null, new MesosferException(e));
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                   MesosferResponse mesosferResponse = new MesosferResponse(response);
                   callback.handle(mesosferResponse, null);
                } catch (MesosferException e) {
                    callback.handle(null, e);
                }
            }
        });
        return this;
    }

    public void cancel() {
        call.cancel();
    }

    public boolean isExecuted() {
        return call.isExecuted();
    }

    public boolean isCanceled() {
        return call.isCanceled();
    }
}
