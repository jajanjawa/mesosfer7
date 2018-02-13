package com.github.jajanjawa.mesosfer7.data;

import com.github.jajanjawa.mesosfer7.MesosferObject;

/**
 * hasil pencarian file
 */
public class FileEntry extends MesosferObject {
    private String bucket;
    private long contentLength;
    private String contentType;
    private String name;
    private String originName;
    private String path;
    private String type;
    private String url;

    public String getBucket() {
        return bucket;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }


    public String getName() {
        return name;
    }


    public String getOriginName() {
        return originName;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }


    public String getUrl() {
        return url;
    }
}
