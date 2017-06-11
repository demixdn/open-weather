package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataEmitter;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;

import java.util.List;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface WeatherRepository {
    void getWeather(@NonNull List<City> cities, @NonNull DataEmitter<Weather> weatherEmitter);
}
