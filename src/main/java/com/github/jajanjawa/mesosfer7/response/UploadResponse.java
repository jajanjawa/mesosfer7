package com.github.jajanjawa.mesosfer7.response;

import com.github.jajanjawa.mesosfer7.request.FileObject;

public class UploadResponse {
    private String objectId;
    private String name;
    private String url;

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public FileObject toFile() {
        FileObject object = new FileObject();
        object.setName(name);
        object.setUrl(url);
        object.setType("file");

        return object;
    }
}
