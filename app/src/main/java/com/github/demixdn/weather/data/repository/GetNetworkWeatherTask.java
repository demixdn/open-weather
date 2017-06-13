package com.github.demixdn.weather.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.JsonParser;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.data.model.WeatherMapper;
import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.github.demixdn.weather.data.network.NetworkException;
import com.github.demixdn.weather.utils.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.QUERY;
import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.UNIT_METRICS;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
class GetNetworkWeatherTask implements Runnable {
    private final NetworkConnection networkConnection;
    private final City city;
    private final Handler handler;
    private final DataCallback<Weather> weatherCallback;

    GetNetworkWeatherTask(@NonNull NetworkConnection networkConnection, @NonNull City city,
                          @NonNull DataCallback<Weather> weatherCallback) {
        this.networkConnection = networkConnection;
        this.city = city;
        this.weatherCallback = weatherCallback;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            String weatherJsonString = networkConnection.getWeatherByCity(city.toQueryString(), QUERY, UNIT_METRICS, null);

            WeatherResponseDTO weatherResponseDTO = JsonParser.parseWeatherJson(weatherJsonString);
            Weather weatherResponse = WeatherMapper.transformFrom(weatherResponseDTO);

            if (weatherResponse != null) {
                emitWeatherItem(weatherResponse);
            }
        } catch (IOException e) {
            Logger.e(e);
        } catch (URISyntaxException | NetworkException e) {
            Logger.e(e);
            emitException(e);
        }
    }

    private void emitWeatherItem(@NonNull final Weather weather) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                weatherCallback.onSuccess(weather);
            }
        });
    }


    private void emitException(final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                weatherCallback.onException(e);
            }
        });
    }
}
