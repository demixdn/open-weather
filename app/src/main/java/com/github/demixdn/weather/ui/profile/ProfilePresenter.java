package com.github.demixdn.weather.ui.profile;

import android.support.annotation.NonNull;

import com.github.demixdn.weather.data.auth.AuthManager;
import com.github.demixdn.weather.ui.base.BasePresenter;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class ProfilePresenter extends BasePresenter<ProfileView> {

    private final AuthManager authManager;

    public ProfilePresenter(@NonNull AuthManager authManager) {
        this.authManager = authManager;
    }

    void viewReady() {
        FirebaseUser user = authManager.getActiveUser();
        assert getView() != null;
        if (user != null) {
            getView().showUser(user);
        }
    }

    void logout() {
        authManager.logout();
    }
}
