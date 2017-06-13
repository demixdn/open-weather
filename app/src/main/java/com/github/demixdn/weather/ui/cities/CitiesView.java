package com.github.demixdn.weather.ui.cities;

import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.ui.base.ProgressView;
import com.github.demixdn.weather.ui.base.View;

import java.util.List;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface CitiesView extends View<CitiesPresenter>, ProgressView {
    void showWeather(List<Weather> weathers);
}
