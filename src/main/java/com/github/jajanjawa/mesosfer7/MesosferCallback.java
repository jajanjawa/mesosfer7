package com.github.jajanjawa.mesosfer7;

public interface MesosferCallback<T> {

    void handle(T result, MesosferException e);
}
