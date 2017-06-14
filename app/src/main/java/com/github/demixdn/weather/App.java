package com.github.demixdn.weather;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
        appComponent = new AppComponent(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private static void init(App app) {
        instance = app;
    }

    public static App getInstance() {
        return instance;
    }
}
