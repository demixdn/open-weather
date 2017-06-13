package com.github.demixdn.weather.data;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.model.Weather;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface WeatherEmitter {
    void onEmit(@NonNull Weather weatherItem);

    void onComplete();

    void onException(@NonNull Exception ex);
}
