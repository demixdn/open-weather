package com.github.demixdn.weather;


import android.app.Application;

public class App extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = new AppComponent(this);
    }

    public static App getInstance() {
        return instance;
    }
}
