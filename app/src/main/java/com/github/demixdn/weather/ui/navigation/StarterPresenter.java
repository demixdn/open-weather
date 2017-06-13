package com.github.demixdn.weather.ui.navigation;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.ui.base.BasePresenter;
import com.github.demixdn.weather.utils.Logger;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class StarterPresenter extends BasePresenter<StartView> {

    private final AuthManager authManager;
    private final CitiesRepository citiesRepository;
    private final DataCallback<List<City>> citiesCallback;

    public StarterPresenter(@NonNull AuthManager authManager, @NonNull CitiesRepository citiesRepository) {
        this.authManager = authManager;
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
        this.citiesRepository.subscribeToCityChanges(citiesCallback);
    }

    void viewReady() {
        FirebaseUser user = authManager.getActiveUser();
        assert getView() != null;
        if (user == null) {
            getView().showLoginScreen();
            return;
        }
        getView().showUser(user);
        citiesRepository.getUserCities(citiesCallback);
    }
}
