package com.github.demixdn.weather;


import android.app.Application;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.demixdn.weather.job.WeatherJobService;
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

    public void startWeatherDispatcher() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        int windowStart = (int) AppConst.EVICTED_PERIOD_SECONDS;
        int windowEnd = windowStart + 60;
        Job myJob = dispatcher.newJobBuilder()
                .setService(WeatherJobService.class)
                .setTag(AppConst.WEATHER_JOB_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    public void cancelWeatherDispatcher() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        dispatcher.cancel(AppConst.WEATHER_JOB_TAG);
    }

    private static void init(App app) {
        instance = app;
    }

    public static App getInstance() {
        return instance;
    }
}
