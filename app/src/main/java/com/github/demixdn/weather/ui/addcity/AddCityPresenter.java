package com.github.demixdn.weather.ui.addcity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.demixdn.weather.data.DataCallback;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.ui.base.BasePresenter;
import com.github.demixdn.weather.utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public class AddCityPresenter extends BasePresenter<AddCityView> {

    private final CitiesRepository repository;
    private Map<String, Boolean> cityStatusCache;

    public AddCityPresenter(@NonNull CitiesRepository repository) {
        this.repository = repository;
        this.cityStatusCache = new HashMap<>();
    }

    void onViewCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            cityStatusCache.clear();
        }
        repository.getAllCities(new DataCallback<List<String>>() {
            @Override
            public void onSuccess(@NonNull List<String> strings) {
                if (getView() != null) {
                    getView().showCities(strings);
                }
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e(ex);
            }
        });
    }

    void addCity(@NonNull final String cityName) {
        cityStatusCache.put(cityName, false);
        repository.addUserCity(cityName, new DataCallback<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                cityStatusCache.put(cityName, aBoolean);
                if (getView() != null) {
                    getView().onAddCityApproved(cityName, aBoolean);
                }
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e(ex);
                cityStatusCache.put(cityName, false);
                if (getView() != null) {
                    getView().onAddCityApproved(cityName, false);
                }
            }
        });
    }

    void removeCity(@NonNull final String cityName) {
        repository.removeUserCity(cityName, new DataCallback<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                if (getView() != null) {
                    getView().onAddRemoveApproved(cityName, aBoolean);
                }
            }

            @Override
            public void onException(@NonNull Exception ex) {
                Logger.e(ex);
                if (getView() != null) {
                    getView().onAddRemoveApproved(cityName, false);
                }
            }
        });
    }

    @NonNull
    public Map<String, Boolean> getCityStatusCache() {
        return cityStatusCache;
    }
}
