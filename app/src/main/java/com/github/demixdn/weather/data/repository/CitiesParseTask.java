package com.github.demixdn.weather.data.repository;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.utils.IOUtils;
import com.github.demixdn.weather.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 07.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class CitiesParseTask implements Runnable {

    private final ParseListener parseListener;
    private final Resources resources;
    private final Handler handler;

    CitiesParseTask(@NonNull ParseListener parseListener, @NonNull Resources resources) {
        this.parseListener = parseListener;
        this.resources = resources;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                parseListener.onComplete(parse());
            }
        });
    }


    private List<String> parse() {
        try {
            String jsonInputString = IOUtils.getStringFrom(resources.openRawResource(R.raw.city_list_min), IOUtils.DEFAULT_BUFFER_SIZE);
            List<City> cities = City.parseJson(jsonInputString);
            List<String> cityNames = new ArrayList<>();
            for (int i = 0; i < cities.size(); i++) {
                City city = cities.get(i);
                cityNames.add(city.toAppString());
            }
            return cityNames;
        } catch (Exception e) {
            Logger.e(e);
            return Collections.emptyList();
        }
    }

    interface ParseListener {
        void onComplete(@NonNull List<String> parseResult);
    }
}
