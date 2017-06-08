package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.model.City;

import java.util.List;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface CitiesRepository {

    @UiThread
    void getAllCities(@NonNull DataCallback<List<String>> callback);

    @UiThread
    void addUserCity(@NonNull String cityNameCountry, @NonNull DataCallback<Boolean> approveCallback);

    @UiThread
    void removeUserCity(@NonNull String cityNameCountry, @NonNull DataCallback<Boolean> approveCallback);

    @UiThread
    void getUserCities(@NonNull DataCallback<List<City>> callback);
}
