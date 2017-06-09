package com.github.demixdn.weather.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;

import java.io.IOException;
import java.util.List;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class JsonParser {
    private JsonParser() {
        //no instance
    }

    @NonNull
    public static List<City> parseCitiesJson(@Nullable String json) throws IOException {
        return LoganSquare.parseList(json, City.class);
    }

    @Nullable
    public static WeatherResponseDTO parseWeatherJson(@Nullable String json) throws IOException {
        return LoganSquare.parse(json, WeatherResponseDTO.class);
    }
}
