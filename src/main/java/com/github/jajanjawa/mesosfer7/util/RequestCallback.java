package com.github.jajanjawa.mesosfer7.util;

import com.github.jajanjawa.mesosfer7.MesosferException;
import com.github.jajanjawa.mesosfer7.MesosferResponse;

public interface RequestCallback {

    void handle(MesosferResponse response, MesosferException e);
}
