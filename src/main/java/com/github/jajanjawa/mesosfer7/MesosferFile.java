package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.data.FileEntry;
import com.github.jajanjawa.mesosfer7.request.FileObject;
import com.github.jajanjawa.mesosfer7.response.UploadResponse;
import com.github.jajanjawa.mesosfer7.util.RequestCallback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;

public class MesosferFile {

    private final MesosferRider rider;
    private String contentType;
    private File file;
    private byte[] content;
    private String name = "";

    public MesosferFile(Application application, String token) {
        rider = new MesosferRider(application);
        rider.authorize(token);
    }

    public MesosferFile contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    private RequestBody constructUploadBody() {
        RequestBody body = null;
        MediaType mediaType = MediaType.parse(contentType);

        if (file != null) {
            body = RequestBody.create(mediaType, file);
        } else if (content != null) {
            body = RequestBody.create(mediaType, content);
        }
        return body;
    }

    public void delete(FileEntry entry) throws MesosferException {
        delete(entry.getObjectId());
    }

    public void delete(FileEntry entry, MesosferCallback<Void> callback) {
        delete(entry.getObjectId(), callback);
    }

    public void delete(String id) throws MesosferException {
        rider.path("files/" + id)
                .delete().execute();
    }

    public void delete(String id, MesosferCallback<Void> callback) {
        rider.path("files/" + id)
                .delete().enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                callback.handle(null, e);
            }
        });
    }

    public UploadResponse upload() throws MesosferException {
        return rider.path("files/" + name)
                .post(constructUploadBody()).execute().as(UploadResponse.class);
    }

    public void upload(MesosferCallback<UploadResponse> callback) {
        rider.path("files/" + name)
                .post(constructUploadBody()).enqueue(new RequestCallback() {
            @Override
            public void handle(MesosferResponse response, MesosferException e) {
                UploadResponse uploadResponse = response != null ? response.as(UploadResponse.class) : null;
                callback.handle(uploadResponse, e);
            }
        });
    }

    public UploadResponse upload(FileObject object) throws MesosferException {
        file = new File(object.getUrl());
        name = object.getName();
        contentType(object.getType());

        return upload();
    }

    /**
     * nama diambil dari file
     * @param file file yang akan diunggah
     * @return MesosferFile
     */
    public MesosferFile file(File file) {
        this.file = file;
        name = file.getName();
        return this;
    }

    public MesosferFile name(String name) {
        this.name = name;
        return this;
    }

    /**
     * perlu setel nama
     * @param data data yang diunggah
     * @return MesosferFile
     * @see #name(String)
     */
    public MesosferFile file(byte[] data) {
        content = data;
        return this;
    }
}
