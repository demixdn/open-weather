package com.github.demixdn.weather.data.auth;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.demixdn.weather.utils.Logger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created on 06.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class AuthManagerImpl implements FacebookCallback<LoginResult>, OnCompleteListener<AuthResult>, AuthManager {
    private final CallbackManager callbackManager;
    private final LoginManager loginManager;
    private final FirebaseAuth firebaseAuth;
    private WeakReference<LoginCallback> loginCallback;

    public AuthManagerImpl(@NonNull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
    }

    @Override
    public void bind(@NonNull LoginCallback loginCallback) {
        this.loginCallback = new WeakReference<>(loginCallback);
        loginManager.registerCallback(callbackManager, this);
    }

    @Override
    public void login(@NonNull Activity activity) {
        loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    @Override
    public boolean isLogged() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Nullable
    @Override
    public FirebaseUser getActiveUser() {
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public void logout() {
        loginManager.logOut();
        firebaseAuth.signOut();
    }

    @Override
    public void unbind() {
        loginCallback = null;
        loginManager.unregisterCallback(callbackManager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Logger.i(this, "Login result is success for userId:" + loginResult.getAccessToken().getUserId());
        needShowDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this);
    }

    @Override
    public void onCancel() {
        Logger.d(this, "cancel");
        IllegalStateException ex = new IllegalStateException("Operation cancelled");
        onResultError(ex);
    }

    @Override
    public void onError(FacebookException error) {
        Logger.e(error);
        onResultError(error);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        onFirebaseComplete(task);
    }

    private void onFirebaseComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Logger.d("signInWithCredential:success " + user.getDisplayName());
                onResultSuccess(user);
            }
        } else {
            Exception taskException = task.getException();
            if (taskException != null) {
                Logger.e(taskException);
                onResultError(taskException);

            }
        }
    }

    private void needShowDialog() {
        if (this.loginCallback == null)
            return;
        LoginCallback loginCallback = this.loginCallback.get();
        if (loginCallback != null) {
            loginCallback.showProgress();
        }
    }

    private void onResultSuccess(FirebaseUser user) {
        if (this.loginCallback == null)
            return;
        LoginCallback loginCallback = this.loginCallback.get();
        if (loginCallback != null) {
            loginCallback.onAuthSuccess(user);
        }
    }

    private void onResultError(Exception error) {
        if (this.loginCallback == null)
            return;
        LoginCallback loginCallback = this.loginCallback.get();
        if (loginCallback != null) {
            loginCallback.onAuthError(error);
        }
    }

}
