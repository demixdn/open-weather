package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.google.firebase.database.DatabaseReference;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
class DispatcherWeatherEmitter implements WeatherEmitter {

    private final WeakReference<WeatherEmitter> weatherEmitter;
    private final Executor weatherExecutor;
    private final NetworkConnection networkConnection;
    private final DatabaseReference database;
    private Deque<City> cityDeque;

    DispatcherWeatherEmitter(@NonNull WeatherEmitter originalEmitter,
                             @NonNull Executor weatherExecutor,
                             @NonNull NetworkConnection networkConnection,
                             @NonNull DatabaseReference database) {
        this.weatherEmitter = new WeakReference<>(originalEmitter);
        this.weatherExecutor = weatherExecutor;
        this.networkConnection = networkConnection;
        this.database = database;
        cityDeque = new ArrayDeque<>();
    }

    void putCity(@NonNull City city) {
        cityDeque.addLast(city);
    }

    @Override
    public void onEmit(@NonNull Weather weatherItem) {
        cityDeque.pollFirst();
        WeatherEmitter weatherEmitter = this.weatherEmitter.get();
        if (weatherEmitter != null) {
            weatherEmitter.onEmit(weatherItem);
            if (cityDeque.isEmpty()) {
                weatherEmitter.onComplete();
            }
        }
    }

    @Override
    public void onComplete() {
        WeatherEmitter weatherEmitter = this.weatherEmitter.get();
        if (weatherEmitter != null) {
            cityDeque.clear();
            weatherEmitter.onComplete();
        }
    }

    @Override
    public void onException(@NonNull Exception ex) {
        City emittedCity = cityDeque.pollFirst();
        if (ex instanceof WeatherNotFindException && emittedCity != null) {
            getWeatherFromNetwork(emittedCity);
        } else {
            WeatherEmitter weatherEmitter = this.weatherEmitter.get();
            if (weatherEmitter != null) {
                weatherEmitter.onException(ex);
            }
        }
    }

    private void getWeatherFromNetwork(@NonNull City city) {
        WeatherNetworkCallback weatherCallback = new WeatherNetworkCallback(database, weatherEmitter);
        weatherExecutor.execute(new GetNetworkWeatherTask(networkConnection, city, weatherCallback));
    }


}
