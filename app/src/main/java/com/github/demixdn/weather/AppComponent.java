package com.github.demixdn.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.FacebookLogin;
import com.github.demixdn.weather.data.FacebookLoginImpl;
import com.github.demixdn.weather.ui.SignInActivity;
import com.github.demixdn.weather.ui.StartActivity;
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
    private FacebookLogin facebookLogin;

    public AppComponent(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    private AppTypeface getAppTypeface() {
        if (appTypeface == null) {
            appTypeface = new AppTypeface(applicationContext);
        }
        return appTypeface;
    }

    private FacebookLogin getFacebookLogin() {
        if (facebookLogin == null) {
            facebookLogin = new FacebookLoginImpl(getFirebaseAuth());
        }
        return facebookLogin;
    }

    private FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public void inject(SignInActivity signInActivity) {
        signInActivity.setAppTypeface(getAppTypeface());
        signInActivity.setFacebookLoginDelegate(getFacebookLogin());
    }

    public void inject(StartActivity startActivity) {
        startActivity.setAppTypeface(getAppTypeface());
        startActivity.setFacebookLoginDelegate(getFacebookLogin());
    }
}
