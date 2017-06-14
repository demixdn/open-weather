package com.github.demixdn.weather.job;

import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.github.demixdn.weather.AppConst;
import com.github.demixdn.weather.data.executor.JobExecutor;
import com.github.demixdn.weather.data.mapper.WeatherMapper;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.github.demixdn.weather.data.parser.JsonParser;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.QUERY;
import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.UNIT_METRICS;

/**
 * Created on 14.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class WeatherJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                final Executor executor = new JobExecutor();
                final NetworkConnection networkConnection = new NetworkConnection();
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<City> cities = new ArrayList<>();
                        for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                            String cityString = citySnapshot.getValue().toString();
                            final City cityToAdd = new City(cityString);
                            if (!cities.contains(cityToAdd)) {
                                cities.add(cityToAdd);
                                executor.execute(new LoadWeatherRunnable(cityToAdd, networkConnection));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //empty
                    }
                });
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private static class LoadWeatherRunnable implements Runnable {

        private final City cityToAdd;
        private final NetworkConnection networkConnection;

        LoadWeatherRunnable(@NonNull City cityToAdd, @NonNull NetworkConnection networkConnection) {
            this.cityToAdd = cityToAdd;
            this.networkConnection = networkConnection;
        }

        @Override
        public void run() {
            try {
                String language = Locale.getDefault().getLanguage();
                String weatherByCity = networkConnection.getWeatherByCity(cityToAdd.toQueryString(), QUERY, UNIT_METRICS, language);
                WeatherResponseDTO responseDTO = JsonParser.parseWeatherJson(weatherByCity);
                Weather weather = WeatherMapper.transformFrom(responseDTO);
                if (weather != null) {
                    putWeatherIntoDatabase(weather);
                }
            } catch (Exception e) {
                Logger.e(e);
            }
        }

        private void putWeatherIntoDatabase(@NonNull Weather weather) {
            DatabaseReference weatherReference = FirebaseDatabase.getInstance().getReference(AppConst.WEATHER_CACHE);
            Map<String, Object> valueMap = new HashMap<>(1);
            valueMap.put(cityToAdd.toDatabaseKey(), weather);
            weatherReference.updateChildren(valueMap);
        }
    }
}
