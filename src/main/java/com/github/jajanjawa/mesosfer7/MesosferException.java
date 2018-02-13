package com.github.jajanjawa.mesosfer7;

import com.google.gson.JsonObject;

public class MesosferException extends Exception {

    private int code;

    public MesosferException(JsonObject error) {
        super(error.get("error").getAsString());
        code = error.get("code").getAsInt();
    }

    public MesosferException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

}
