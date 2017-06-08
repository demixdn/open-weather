package com.github.demixdn.weather.ui.addcity;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.ui.base.View;

import java.util.List;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface AddCityView extends View<AddCityPresenter> {

    void showCities(@NonNull List<String> cities);

    void onAddCityApproved(@NonNull String cityName, boolean result);

    void onAddRemoveApproved(@NonNull String cityName, boolean result);
}
