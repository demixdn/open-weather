package com.github.demixdn.weather.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.ui.AboutFragment;
import com.github.demixdn.weather.ui.profile.ProfileFragment;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.addcity.AddCityActivity;
import com.github.demixdn.weather.ui.cities.CitiesFragment;
import com.github.demixdn.weather.ui.cities.EmptyCitiesFragment;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class Navigator {

    public void showSignIn(Activity context, int requestCode){
        SignInActivity.navigate(context, requestCode);
    }

    public void showAddCity(Context context){
        AddCityActivity.navigate(context);
    }

    public void showEmptyState(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new EmptyCitiesFragment())
                .commitAllowingStateLoss();
    }

    public void showCities(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new CitiesFragment())
                .commitAllowingStateLoss();
    }

    public void showProfile(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commitAllowingStateLoss();
    }

    public void showInfo(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new AboutFragment())
                .commitAllowingStateLoss();
    }

    public void showDetail(FragmentManager fragmentManager){

    }
}