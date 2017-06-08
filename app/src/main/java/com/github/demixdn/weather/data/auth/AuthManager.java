package com.github.demixdn.weather.data.auth;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created on 06.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public interface AuthManager {
    void bind(@NonNull LoginCallback loginCallback);

    void login(@NonNull Activity activity);

    boolean isLogged();

    @Nullable
    FirebaseUser getActiveUser();

    void logout();

    void unbind();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
