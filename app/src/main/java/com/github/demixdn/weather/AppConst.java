package com.github.demixdn.weather;

/**
 * Created on 14.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class AppConst {
    public static final long EVICTED_PERIOD_SECONDS = 2 * 60 * 60L;
    public static final long EVICTED_PERIOD_MILLIS = 2 * 60 * 60 * 1000L;
    public static final String WEATHER_CACHE = "weather_cache";
    public static final String WEATHER_JOB_TAG = "com.github.demixdn.weather.weatherdispatcher";

    private AppConst() {
        //no instance
    }
}
