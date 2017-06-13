package com.github.demixdn.weather;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.auth.AuthManagerImpl;
import com.github.demixdn.weather.data.executor.JobExecutor;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.data.repository.CitiesRepositoryImpl;
import com.github.demixdn.weather.data.repository.WeatherRepository;
import com.github.demixdn.weather.data.repository.WeatherRepositoryImpl;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.navigation.StartActivity;
import com.github.demixdn.weather.ui.addcity.AddCityPresenter;
import com.github.demixdn.weather.ui.addcity.AddCityView;
import com.github.demixdn.weather.ui.cities.CitiesPresenter;
import com.github.demixdn.weather.ui.cities.CitiesView;
import com.github.demixdn.weather.ui.navigation.StarterPresenter;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

/**
 * Created on 06.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class AppComponent {

    private final Context applicationContext;
    private AppTypeface appTypeface;
    private AuthManager authManager;
    private AddCityPresenter addCityPresenter;
    private Resources resources;
    private CitiesRepository citiesRepository;
    private WeatherRepository weatherRepository;
    private NetworkConnection networkConnection;
    private CitiesPresenter citiesPresenter;
    private StarterPresenter starterPresenter;
    private JobExecutor jobExecutor;

    public AppComponent(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    private synchronized AppTypeface getAppTypeface() {
        if (appTypeface == null) {
            appTypeface = new AppTypeface(applicationContext);
        }
        return appTypeface;
    }

    private synchronized AuthManager getAuthManager() {
        if (authManager == null) {
            authManager = new AuthManagerImpl(getFirebaseAuth());
        }
        return authManager;
    }

    private synchronized FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    private synchronized AddCityPresenter getAddCityPresenter() {
        if (addCityPresenter == null) {
            addCityPresenter = new AddCityPresenter(getCitiesRepository());
        }
        return addCityPresenter;
    }

    private synchronized CitiesRepository getCitiesRepository() {
        if (citiesRepository == null) {
            citiesRepository = new CitiesRepositoryImpl(getResources(), getAuthManager());
        }
        return citiesRepository;
    }

    private synchronized Resources getResources() {
        if (resources == null) {
            resources = applicationContext.getResources();
        }
        return resources;
    }

    private synchronized NetworkConnection getNetworkConnection() {
        if (networkConnection == null) {
            networkConnection = new NetworkConnection();
        }
        return networkConnection;
    }

    private synchronized Executor getExecutor() {
        if (jobExecutor == null) {
            jobExecutor = new JobExecutor();
        }
        return jobExecutor;
    }

    private synchronized WeatherRepository getWeatherRepository() {
        if (weatherRepository == null) {
            weatherRepository = new WeatherRepositoryImpl(getNetworkConnection(), getExecutor());
        }
        return weatherRepository;
    }

    private synchronized CitiesPresenter getCitiesPresenter() {
        if (citiesPresenter == null) {
            citiesPresenter = new CitiesPresenter(getCitiesRepository(), getWeatherRepository());
        }
        return citiesPresenter;
    }

    private StarterPresenter getStarterPresenter() {
        if (starterPresenter == null) {
            starterPresenter = new StarterPresenter(getAuthManager(), getCitiesRepository());
        }
        return starterPresenter;
    }

    public void inject(SignInActivity signInActivity) {
        signInActivity.setAppTypeface(getAppTypeface());
        signInActivity.setAuthDelegate(getAuthManager());
    }

    public void inject(StartActivity startActivity) {
        startActivity.setAppTypeface(getAppTypeface());
        startActivity.bindPresenter(getStarterPresenter());
        startActivity.getPresenter().bindView(startActivity);
    }

    public void inject(AddCityView view) {
        view.bindPresenter(getAddCityPresenter());
        view.getPresenter().bindView(view);
    }

    public void inject(CitiesView view) {
        view.bindPresenter(getCitiesPresenter());
        view.getPresenter().bindView(view);
    }
}
