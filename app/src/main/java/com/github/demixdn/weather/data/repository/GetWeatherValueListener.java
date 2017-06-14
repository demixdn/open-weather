package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.Weather;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
class GetWeatherValueListener implements ValueEventListener {
    private final WeakReference<WeatherEmitter> weatherEmitter;

    GetWeatherValueListener(@NonNull WeatherEmitter weatherEmitter) {
        this.weatherEmitter = new WeakReference<>(weatherEmitter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Weather weather = dataSnapshot.getValue(Weather.class);
        if (weather == null) {
            emitException(new WeatherNotFindException());
        } else {
            if (weather.isEvicted()) {
                emitException(new WeatherNotFindException());
            } else {
                emitWeatherItem(weather);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        emitException(databaseError.toException());
    }

    private void emitWeatherItem(@NonNull final Weather weather) {
        WeatherEmitter emitter = weatherEmitter.get();
        if (emitter != null) {
            emitter.onEmit(weather);
        }
    }

    private void emitException(final Exception e) {
        WeatherEmitter emitter = weatherEmitter.get();
        if (emitter != null) {
            emitter.onException(e);
        }
    }

}
