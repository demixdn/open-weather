package com.github.demixdn.weather.ui.navigation;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.ui.base.View;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface StartView extends View<StarterPresenter> {
    void showLoginScreen();

    void showUser(@NonNull FirebaseUser user);

    void showEmptyState();

    void showCities();
}
