package com.github.demixdn.weather.data.database;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class WeatherDiskDataStoreImpl implements WeatherDiskDataStore {

    private final DatabaseReference database;

    public WeatherDiskDataStoreImpl(FirebaseDatabase firebaseDatabase) {
        this.database = firebaseDatabase.getReference("weather_cache");
        database.keepSynced(false);
    }

    @Override
    public void putWeather(@NonNull Weather weather) {
        Map<String, Object> valueMap = new HashMap<>(1);
        valueMap.put(weather.getCity().toDatabaseKey(), weather);
        database.updateChildren(valueMap);
    }

    @Override
    public void getWeather(@NonNull City city, @NonNull final DataCallback<Weather> weatherCallback) {
        database.child(city.toDatabaseKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Weather weather = dataSnapshot.getValue(Weather.class);
                weatherCallback.onSuccess(weather);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                weatherCallback.onException(databaseError.toException());
            }
        });
    }
}
