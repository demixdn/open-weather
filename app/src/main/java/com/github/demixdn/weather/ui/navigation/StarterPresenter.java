package com.github.demixdn.weather.ui.navigation;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.ui.base.BasePresenter;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class StarterPresenter extends BasePresenter<StartView> {

    private final FirebaseAuth authManager;
    private final CitiesRepository citiesRepository;
    private final DataCallback<List<City>> citiesCallback;

    public StarterPresenter(@NonNull FirebaseAuth firebaseAuth, @NonNull final CitiesRepository citiesRepository) {
        this.authManager = firebaseAuth;
        this.citiesRepository = citiesRepository;
        citiesCallback = new DataCallback<List<City>>() {
            @Override
            public void onSuccess(@NonNull List<City> cities) {
                if (getView() != null) {
                    if (cities.isEmpty()) {
                        getView().showEmptyState();
                    } else {
                        getView().showCities();
                    }
                }
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e(ex);
                if (getView() != null) {
                    getView().showEmptyState();
                }
            }
        };
        subscribe();
        this.authManager.addAuthStateListener(new StarterAuthStateListener());
    }

    void viewReady() {
        citiesRepository.getUserCities(citiesCallback);
        FirebaseUser user = authManager.getCurrentUser();
        if (user != null && getView() != null) {
            getView().showUser(user);

        }
    }

    void subscribe() {
        this.citiesRepository.subscribeToCityChanges(citiesCallback);
    }

    private class StarterAuthStateListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = authManager.getCurrentUser();
            if (user == null) {
                App.getInstance().cancelWeatherDispatcher();
                if (getView() != null) {
                    getView().showLoginScreen();
                }
            } else {
                App.getInstance().startWeatherDispatcher();
                if (getView() != null) {
                    getView().showUser(user);
                }
            }
        }
    }
}
