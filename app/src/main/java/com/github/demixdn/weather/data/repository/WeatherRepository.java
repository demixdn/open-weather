package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.City;

import java.util.List;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface WeatherRepository {
    void getWeather(@NonNull List<City> cities, @NonNull WeatherEmitter weatherEmitter);

    void removeWeather(@NonNull City forCity, @NonNull DataCallback<Boolean> approveCallback);
}
