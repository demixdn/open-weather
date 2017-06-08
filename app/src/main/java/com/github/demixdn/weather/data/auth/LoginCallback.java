package com.github.demixdn.weather.data.auth;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created on 06.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
public interface LoginCallback {
    void onAuthSuccess(@NonNull FirebaseUser user);

    void onAuthError(Exception ex);
}
