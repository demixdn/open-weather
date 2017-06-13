package com.github.demixdn.weather.data.model;

import android.support.annotation.Nullable;

import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class WeatherMapper {
    private WeatherMapper() {
        //no instance
    }

    @Nullable
    public static Weather transformFrom(@Nullable WeatherResponseDTO response) {
        if (response == null) {
            return null;
        }

        Weather weather = new Weather();
        weather.setCity(new City(response.cityName, response.system.country.toUpperCase()));
        weather.setConditionDescription(response.conditionList.get(0).description);
        weather.setConditionTitle(response.conditionList.get(0).main);
        weather.setConditionIcon(response.conditionList.get(0).icon);
        weather.setTemp(response.mainInfo.temp);
        weather.setTempMin(response.mainInfo.tempMin);
        weather.setTempMax(response.mainInfo.tempMax);
        weather.setPressure(response.mainInfo.pressure);
        weather.setHumidity(response.mainInfo.humidity);
        weather.setWindSpeed(response.wind.speed);
        weather.setWindDegrees(response.wind.deg);
        weather.setWeatherTime(response.dateInSeconds);
        return weather;
    }
}
