package com.github.demixdn.weather;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.Observable;
import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.auth.AuthManagerImpl;
import com.github.demixdn.weather.data.executor.JobExecutor;
import com.github.demixdn.weather.data.network.NetworkConnection;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.data.repository.CitiesRepositoryImpl;
import com.github.demixdn.weather.data.repository.WeatherRepository;
import com.github.demixdn.weather.data.repository.WeatherRepositoryImpl;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.addcity.AddCityActivity;
import com.github.demixdn.weather.ui.addcity.AddCityPresenter;
import com.github.demixdn.weather.ui.cities.CitiesFragment;
import com.github.demixdn.weather.ui.cities.CitiesPresenter;
import com.github.demixdn.weather.ui.navigation.StartActivity;
import com.github.demixdn.weather.ui.navigation.StarterPresenter;
import com.github.demixdn.weather.ui.profile.ProfileFragment;
import com.github.demixdn.weather.ui.profile.ProfilePresenter;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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

    private Resources resources;

    private JobExecutor jobExecutor;

    private NetworkConnection networkConnection;

    private WeatherRepository weatherRepository;
    private CitiesRepository citiesRepository;

    private AddCityPresenter addCityPresenter;
    private CitiesPresenter citiesPresenter;
    private StarterPresenter starterPresenter;
    private ProfilePresenter profilePresenter;
    private Observable reloadObservalble;

    public AppComponent(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        this.reloadObservalble = new Observable();
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
        createCitiesRepo();
        return citiesRepository;
    }

    private void createCitiesRepo() {
        if (citiesRepository == null) {
            citiesRepository = new CitiesRepositoryImpl(getResources(), getAuthManager(), FirebaseDatabase.getInstance());
        }
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
        createWeatherRepo();
        return weatherRepository;
    }

    private void createWeatherRepo() {
        if (weatherRepository == null) {
            weatherRepository = new WeatherRepositoryImpl(getNetworkConnection(), getExecutor(), FirebaseDatabase.getInstance());
        }
    }

    private synchronized CitiesPresenter getCitiesPresenter() {
        if (citiesPresenter == null) {
            citiesPresenter = new CitiesPresenter(getCitiesRepository(), getWeatherRepository());
        }
        return citiesPresenter;
    }

    private synchronized StarterPresenter getStarterPresenter() {
        if (starterPresenter == null) {
            starterPresenter = new StarterPresenter(getFirebaseAuth(), getCitiesRepository());
        }
        return starterPresenter;
    }

    private synchronized ProfilePresenter getProfilePresenter() {
        if (profilePresenter == null) {
            profilePresenter = new ProfilePresenter(getAuthManager());
        }
        return profilePresenter;
    }

    public synchronized void reload() {
        weatherRepository = new WeatherRepositoryImpl(getNetworkConnection(), getExecutor(), FirebaseDatabase.getInstance());
        citiesRepository = new CitiesRepositoryImpl(getResources(), getAuthManager(), FirebaseDatabase.getInstance());

        addCityPresenter = new AddCityPresenter(getCitiesRepository());
        citiesPresenter = new CitiesPresenter(getCitiesRepository(), getWeatherRepository());
        starterPresenter = new StarterPresenter(getFirebaseAuth(), getCitiesRepository());
        profilePresenter = new ProfilePresenter(getAuthManager());
        reloadObservalble.notifyObservers();
    }

    public void inject(SignInActivity signInActivity) {
        signInActivity.setAppTypeface(getAppTypeface());
        signInActivity.setAuthDelegate(getAuthManager());
    }

    public void inject(StartActivity startActivity) {
        reloadObservalble.addObserver(startActivity);
        startActivity.setAppTypeface(getAppTypeface());
        startActivity.bindPresenter(getStarterPresenter());
        startActivity.getPresenter().bindView(startActivity);
    }

    public void inject(AddCityActivity view) {
        reloadObservalble.addObserver(view);
        view.bindPresenter(getAddCityPresenter());
        view.getPresenter().bindView(view);
    }

    public void inject(CitiesFragment view) {
        reloadObservalble.addObserver(view);
        view.bindPresenter(getCitiesPresenter());
        view.getPresenter().bindView(view);
    }

    public void inject(ProfileFragment profileFragment) {
        profileFragment.setAppTypeface(getAppTypeface());
        profileFragment.bindPresenter(getProfilePresenter());
        profileFragment.getPresenter().bindView(profileFragment);
    }
}
