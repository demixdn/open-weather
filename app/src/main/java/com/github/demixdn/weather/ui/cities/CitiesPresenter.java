package com.github.demixdn.weather.ui.cities;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.data.repository.WeatherRepository;
import com.github.demixdn.weather.ui.base.BasePresenter;
import com.github.demixdn.weather.utils.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class CitiesPresenter extends BasePresenter<CitiesView> {
    @NonNull
    private final CitiesRepository citiesRepository;
    @NonNull
    private final WeatherRepository weatherRepository;
    private CitiesWeatherEmitter weatherEmitter;

    public CitiesPresenter(@NonNull CitiesRepository citiesRepository, @NonNull WeatherRepository weatherRepository) {
        this.citiesRepository = citiesRepository;
        this.weatherRepository = weatherRepository;
    }

    void loadWeather() {
        Logger.i(this, "loadWeather start");
        if (getView() != null) {
            getView().showProgress();
        }
        weatherEmitter = new CitiesWeatherEmitter();
        citiesRepository.getUserCities(new DataCallback<List<City>>() {
            @Override
            public void onSuccess(@NonNull List<City> cities) {
                weatherRepository.getWeather(cities, weatherEmitter);
//                Logger.d(cities.toString());
            }

            @Override
            public void onException(@NonNull Exception ex) {
                if (getView() != null) {
                    getView().showError(ex.getMessage());
                }
            }
        });
    }

    void onCityRemove(@NonNull City city){
        weatherRepository.removeWeather(city, new DataCallback<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                Logger.d(this, aBoolean ? "Weather for city removed" : "City not removed");
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e("Weather for city remove exception", ex);
            }
        });
        citiesRepository.removeUserCity(city.toAppString(), new DataCallback<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                Logger.d(this, aBoolean ? "City removed" : "City not removed");
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e("City remove exception", ex);
            }
        });
    }

    @Override
    public void unbindView() {
        weatherEmitter = null;
        super.unbindView();
    }

    private class CitiesWeatherEmitter implements WeatherEmitter {
        @Override
        public void onEmit(@NonNull Weather weatherItem) {
            if (getView() != null) {
                getView().showWeather(Arrays.asList(weatherItem));
            }
        }

        @Override
        public void onComplete() {
            if (getView() != null) {
                getView().hideProgress();
            }
        }

        @Override
        public void onException(@NonNull Exception ex) {
            if (getView() != null) {
                getView().hideProgress();
                getView().showError(ex.getMessage());
            }
        }
    }
}