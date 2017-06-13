package com.github.demixdn.weather.ui.profile;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.ui.base.View;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface ProfileView extends View<ProfilePresenter> {
    void showUser(@NonNull FirebaseUser user);
}
