package com.github.demixdn.weather.data.model;

import com.github.demixdn.weather.AppConst;

import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
public class WeatherTest {

    @Test
    public void isEvicted_true() throws Exception {
        Weather weather = new Weather();
        long threeHoursAndOneSeconds = AppConst.EVICTED_PERIOD_MILLIS + 1000L;
        long weatherTime = Calendar.getInstance().getTimeInMillis() - threeHoursAndOneSeconds;
        weather.setWeatherTime(weatherTime/1000);

        assertThat(weather.isEvicted(), is(true));
    }

    @Test
    public void isEvicted_false() throws Exception {
        Weather weather = new Weather();
        long threeHoursWithoutOneSeconds = AppConst.EVICTED_PERIOD_MILLIS - 1000L;
        long weatherTime = Calendar.getInstance().getTimeInMillis() - threeHoursWithoutOneSeconds;
        weather.setWeatherTime(weatherTime/1000);

        assertThat(weather.isEvicted(), is(false));
    }

}