package com.github.demixdn.weather.data.repository;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class CitiesRepositoryImpl implements CitiesRepository {

    private final Resources resources;
    private final DatabaseReference userReference;
    private CitiesValueEventListener valueEventListener;
    private ValueEventListener subscribeValueEventListener;

    public CitiesRepositoryImpl(@NonNull Resources resources, @NonNull AuthManager authManager, @NonNull FirebaseDatabase database) {
        this.resources = resources;
        FirebaseUser currentUser = authManager.getActiveUser();
        if (currentUser == null) {
            userReference = null;
        } else {
            userReference = database.getReference().child(currentUser.getUid());
        }
    }

    @Override
    public void getAllCities(@NonNull DataCallback<List<String>> callback) {
        CitiesParseTask parseTask = new CitiesParseTask(new CitiesParseListener(callback), resources);
        Executors.newSingleThreadExecutor().execute(parseTask);
    }

    @Override
    public void addUserCity(@NonNull final String cityNameCountry, @NonNull final DataCallback<Boolean> approveCallback) {
        if (userReference == null) {
            sendFalse(approveCallback);
        } else {
            String key = userReference.push().getKey();
            HashMap<String, Object> cityMap = new HashMap<>();
            cityMap.put(key, cityNameCountry);
            userReference.updateChildren(cityMap, new AddCityCompletionListener(approveCallback));
        }
    }

    @Override
    public void removeUserCity(@NonNull final String cityNameCountry, @NonNull final DataCallback<Boolean> approveCallback) {
        if (userReference == null) {
            sendFalse(approveCallback);
        } else {
            userReference.addListenerForSingleValueEvent(new RemoveCityValueEventListener(cityNameCountry, approveCallback));
        }
    }

    @Override
    public void getUserCities(@NonNull final DataCallback<List<City>> callback) {
        if (userReference == null) {
            callback.onSuccess(Collections.<City>emptyList());
        } else {
            if (valueEventListener != null) {
                userReference.removeEventListener(valueEventListener);
            }
            valueEventListener = new CitiesValueEventListener(callback);
            userReference.addValueEventListener(valueEventListener);
        }
    }

    @Override
    public void subscribeToCityChanges(@NonNull DataCallback<List<City>> callback) {
        if (userReference == null) {
            callback.onSuccess(Collections.<City>emptyList());
        } else {
            if (subscribeValueEventListener != null) {
                userReference.removeEventListener(subscribeValueEventListener);
            }
            subscribeValueEventListener = new CitiesValueEventListener(callback);
            userReference.addValueEventListener(subscribeValueEventListener);
        }
    }

    private void sendFalse(@NonNull final DataCallback<Boolean> approveCallback) {
        approveCallback.onSuccess(false);
    }

    private static class CitiesParseListener implements CitiesParseTask.ParseListener {
        private final DataCallback<List<String>> callback;

        CitiesParseListener(DataCallback<List<String>> callback) {
            this.callback = callback;
        }

        @Override
        public void onComplete(ParseResult parseResult) {
            List<String> cities = parseResult.cities;
            if (callback != null) {
                callback.onSuccess(cities);
            }
        }
    }

    private static class AddCityCompletionListener implements DatabaseReference.CompletionListener {
        private final WeakReference<DataCallback<Boolean>> approveCallback;

        AddCityCompletionListener(DataCallback<Boolean> approveCallback) {
            this.approveCallback = new WeakReference<>(approveCallback);
        }

        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            DataCallback<Boolean> callback = approveCallback.get();
            if (callback != null) {
                if (databaseError != null) {
                    callback.onException(databaseError.toException());
                } else {
                    callback.onSuccess(true);
                }
            }
        }
    }

    private static class CitiesValueEventListener implements ValueEventListener {
        private final WeakReference<DataCallback<List<City>>> callback;

        CitiesValueEventListener(DataCallback<List<City>> callback) {
            this.callback = new WeakReference<>(callback);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<City> cities = new ArrayList<>();
            for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                String cityString = citySnapshot.getValue().toString();
                City cityToAdd = new City(cityString);
                if (!cities.contains(cityToAdd)) {
                    cities.add(cityToAdd);
                }
            }
            Logger.d("CitiesRepo", "Cities changed :: " + cities.toString());
            DataCallback<List<City>> dataCallback = callback.get();
            if (dataCallback != null) {
                dataCallback.onSuccess(cities);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            DataCallback<List<City>> dataCallback = callback.get();
            if (dataCallback != null) {
                dataCallback.onException(databaseError.toException());
            }
        }
    }

    private static class RemoveCityValueEventListener implements ValueEventListener {

        private final String cityNameCountry;
        private final WeakReference<DataCallback<Boolean>> approveCallback;

        RemoveCityValueEventListener(String cityNameCountry, DataCallback<Boolean> approveCallback) {
            this.cityNameCountry = cityNameCountry;
            this.approveCallback = new WeakReference<>(approveCallback);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> snapshotIterator = dataSnapshot.getChildren().iterator();
            while (snapshotIterator.hasNext()) {
                DataSnapshot citySnapshot = snapshotIterator.next();
                String cityString = citySnapshot.getValue().toString();
                if (cityNameCountry.contains(cityString)) {
                    removeValueIn(citySnapshot);
                    break;
                }
            }
        }

        private void removeValueIn(DataSnapshot citySnapshot) {
            DatabaseReference ref = citySnapshot.getRef();
            ref.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    DataCallback<Boolean> callback = approveCallback.get();
                    if (callback != null) {
                        if (databaseError != null) {
                            callback.onException(databaseError.toException());
                        } else {
                            callback.onSuccess(true);
                        }
                    }
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            DataCallback<Boolean> callback = approveCallback.get();
            if (callback != null) {
                callback.onException(databaseError.toException());
            }
        }
    }
}
