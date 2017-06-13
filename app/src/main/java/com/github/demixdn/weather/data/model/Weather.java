package com.github.demixdn.weather.data.model;

import com.github.demixdn.weather.utils.Logger;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class Weather implements Serializable {
    private final static long EVICTED_PERIOD = 2 * 60 * 60L;

    private City city;
    private String conditionTitle;
    private String conditionDescription;
    private String conditionIcon;
    private double temp;
    private double tempMin;
    private double tempMax;
    private double pressure;
    private int humidity;
    private double windSpeed;
    private double windDegrees;
    private long weatherTimeInSeconds;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        Logger.d(this, "Set city " + city.toString());
        this.city = city;
    }

    public String getConditionTitle() {
        return conditionTitle;
    }

    public void setConditionTitle(String conditionTitle) {
        this.conditionTitle = conditionTitle;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(String conditionIcon) {
        this.conditionIcon = conditionIcon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(double windDegrees) {
        this.windDegrees = windDegrees;
    }

    public long getWeatherTime() {
        return weatherTimeInSeconds;
    }

    public void setWeatherTime(long weatherTime) {
        this.weatherTimeInSeconds = weatherTime;
    }

    public boolean isEvicted() {
        long currentInSeconds = Calendar.getInstance().getTimeInMillis() / 1000L;
        long diffInSec = currentInSeconds - weatherTimeInSeconds;
        return diffInSec > EVICTED_PERIOD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (weatherTimeInSeconds != weather.weatherTimeInSeconds) return false;
        return city.equals(weather.city);

    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + (int) (weatherTimeInSeconds ^ (weatherTimeInSeconds >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city=" + city +
                ", conditionTitle='" + conditionTitle + '\'' +
                ", temp=" + temp +
                '}';
    }
}
