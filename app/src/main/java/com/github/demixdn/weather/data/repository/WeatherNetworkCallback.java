package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.Weather;
import com.google.firebase.database.DatabaseReference;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
class WeatherNetworkCallback implements DataCallback<Weather> {
    private final DatabaseReference database;
    private final WeakReference<WeatherEmitter> weatherEmitter;

    WeatherNetworkCallback(@NonNull DatabaseReference database,
                           @NonNull WeakReference<WeatherEmitter> weatherEmitter) {
        this.database = database;
        this.weatherEmitter = weatherEmitter;
    }

    @Override
    public void onSuccess(@NonNull Weather weather) {
        Map<String, Object> valueMap = new HashMap<>(1);
        valueMap.put(weather.getCity().toDatabaseKey(), weather);
        database.updateChildren(valueMap);
    }

    @Override
    public void onException(@NonNull Exception ex) {
        WeatherEmitter weatherEmitter = this.weatherEmitter.get();
        if (weatherEmitter != null) {
            weatherEmitter.onException(ex);
        }
    }
}
