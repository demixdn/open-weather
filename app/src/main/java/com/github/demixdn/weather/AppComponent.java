package com.github.demixdn.weather;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.data.auth.AuthManagerImpl;
import com.github.demixdn.weather.data.repository.CitiesRepository;
import com.github.demixdn.weather.data.repository.CitiesRepositoryImpl;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.StartActivity;
import com.github.demixdn.weather.ui.addcity.AddCityPresenter;
import com.github.demixdn.weather.ui.addcity.AddCityView;
import com.github.demixdn.weather.utils.AppTypeface;
import com.google.firebase.auth.FirebaseAuth;

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
    private CitiesRepositoryImpl citiesRepository;

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

    public void inject(SignInActivity signInActivity) {
        signInActivity.setAppTypeface(getAppTypeface());
        signInActivity.setFacebookLoginDelegate(getAuthManager());
    }

    public void inject(StartActivity startActivity) {
        startActivity.setAppTypeface(getAppTypeface());
        startActivity.setFacebookLoginDelegate(getAuthManager());
        startActivity.setCitiesRepository(getCitiesRepository());
    }

    public void inject(AddCityView view) {
        view.bindPresenter(getAddCityPresenter());
        view.getPresenter().bindView(view);
    }
}
