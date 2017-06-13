package com.github.demixdn.weather.data.model;

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
        long threeHoursAndOneSeconds = 3 * 60 * (60 + 1) * 1000L;
        long weatherTime = Calendar.getInstance().getTimeInMillis() - threeHoursAndOneSeconds;
        weather.setWeatherTime(weatherTime/1000);

        assertThat(weather.isEvicted(), is(true));
    }

    @Test
    public void isEvicted_false() throws Exception {
        Weather weather = new Weather();
        long threeHoursWithoutOneSeconds = 3 * 60 * (60 - 1) * 1000L;
        long weatherTime = Calendar.getInstance().getTimeInMillis() - threeHoursWithoutOneSeconds;
        weather.setWeatherTime(weatherTime/1000);

        assertThat(weather.isEvicted(), is(false));
    }

}