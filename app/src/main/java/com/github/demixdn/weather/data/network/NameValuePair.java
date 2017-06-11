package com.github.demixdn.weather.data.network;

import android.support.annotation.NonNull;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */

class NameValuePair {

    final String name;
    final String value;

    NameValuePair(@NonNull String name, @NonNull String value) {
        this.name = name;
        this.value = value;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }
}
