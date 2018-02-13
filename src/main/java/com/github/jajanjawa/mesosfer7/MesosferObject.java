package com.github.jajanjawa.mesosfer7;

import com.github.jajanjawa.mesosfer7.util.DateFormat;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Objek yang bisa disimpan pada mesosfer.
 */
public class MesosferObject {
    protected Date createdAt;
    protected String objectId;
    protected Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(JsonObject result) {
        String dateString = result.get("createdAt").getAsString();
        createdAt = DateFormat.getInstance().parseServer(dateString);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    void setObjectId(JsonObject result) {
        objectId = result.get("objectId").getAsString();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    void setUpdatedAt(JsonObject result) {
        String dateString = result.get("updatedAt").getAsString();
        updatedAt = DateFormat.getInstance().parseServer(dateString);
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void reset() {
        objectId = null;
        resetTime();
    }

    public void resetTime() {
        updatedAt = null;
        createdAt = null;
    }
}
