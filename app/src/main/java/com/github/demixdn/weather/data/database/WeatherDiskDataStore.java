package com.github.demixdn.weather.data.database;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface WeatherDiskDataStore {
    void putWeather(@NonNull Weather weather);

    void getWeather(@NonNull City city, @NonNull DataCallback<Weather> weatherCallback);
}
