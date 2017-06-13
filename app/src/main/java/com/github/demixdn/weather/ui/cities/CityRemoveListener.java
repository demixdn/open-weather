package com.github.demixdn.weather.ui.cities;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.model.City;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface CityRemoveListener {
    void onCityRemoved(@NonNull City removedCity);
}
