package com.github.demixdn.weather.data.repository;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.WeatherEmitter;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class WeatherRepositoryImpl implements WeatherRepository {

    private final NetworkConnection networkConnection;
    private final DatabaseReference database;
    private final Executor weatherExecutor;


    public WeatherRepositoryImpl(@NonNull NetworkConnection networkConnection,
                                 @NonNull Executor weatherExecutor) {
        this.networkConnection = networkConnection;
        this.database = getWeatherReference();
        this.database.keepSynced(true);
        this.weatherExecutor = weatherExecutor;
    }

    private DatabaseReference getWeatherReference() {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();
        return userReference.child("weather_cache");
    }

    @Override
    public void getWeather(@NonNull List<City> cities, @NonNull final WeatherEmitter weatherEmitter) {
        int citiesSize = cities.size();
        DispatcherWeatherEmitter dispatcherWeatherEmitter = new DispatcherWeatherEmitter(weatherEmitter, weatherExecutor, networkConnection, database);
        GetWeatherValueListener getWeatherValueListener = new GetWeatherValueListener(dispatcherWeatherEmitter);
        for (int i = 0; i < citiesSize; i++) {
            City city = cities.get(i);
            final DatabaseReference cityReference = database.child(city.toDatabaseKey());
            dispatcherWeatherEmitter.putCity(city);
            cityReference.addValueEventListener(getWeatherValueListener);
        }
    }

    @Override
    public void removeWeather(@NonNull City forCity, @NonNull final DataCallback<Boolean> approveCallback) {
        final DatabaseReference cityReference = database.child(forCity.toDatabaseKey());
        cityReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    approveCallback.onException(databaseError.toException());
                } else {
                    approveCallback.onSuccess(true);
                }
            }
        });
    }

}

