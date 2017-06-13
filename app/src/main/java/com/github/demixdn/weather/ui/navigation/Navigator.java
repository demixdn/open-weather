package com.github.demixdn.weather.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.ui.AboutFragment;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.addcity.AddCityActivity;
import com.github.demixdn.weather.ui.cities.CitiesFragment;
import com.github.demixdn.weather.ui.cities.EmptyCitiesFragment;
import com.github.demixdn.weather.ui.profile.ProfileFragment;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class Navigator {

    void showSignIn(Activity context, int requestCode) {
        SignInActivity.navigate(context, requestCode);
    }

    void showAddCity(Context context) {
        AddCityActivity.navigate(context);
    }

    void showEmptyState(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new EmptyCitiesFragment())
                .commitAllowingStateLoss();
    }

    void showCities(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new CitiesFragment())
                .commitAllowingStateLoss();
    }

    void showProfile(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commitAllowingStateLoss();
    }

    void showInfo(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new AboutFragment())
                .commitAllowingStateLoss();
    }

    void showDetail(Context context, Weather weather) {
//        MapsActivity.navigate(context);
    }
}
