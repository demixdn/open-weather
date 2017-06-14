package com.github.demixdn.weather.data.repository;

/**
 * Created on 14.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class WeatherNotFindException extends Exception {
    public WeatherNotFindException() {
        super("Weather item not find");
    }
}
