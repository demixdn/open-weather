package com.github.demixdn.weather.data;

import android.support.annotation.NonNull;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface DataCallback<Data> {
    void onSuccess(@NonNull Data data);

    void onException(@NonNull Exception ex);
}
