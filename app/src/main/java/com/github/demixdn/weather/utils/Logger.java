package com.github.demixdn.weather.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.demixdn.weather.BuildConfig;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Logger {

    private static boolean developerMode = BuildConfig.DEBUG;
    private static final String TAG = "Logger";

    private Logger() {
        //empty
    }

    public static void setEnabled(boolean enabled) {
        developerMode = enabled;
    }

    public static void e(@NonNull String tag, @NonNull String message, @NonNull Throwable throwable) {
        if (developerMode) {
            throwable.printStackTrace();
            Log.e(tag, message, throwable);
        }
    }

    public static void i(@NonNull String tag, @NonNull String message) {
        if (developerMode) {
            Log.i(tag, message);
        }
    }

    public static void d(@NonNull String tag, @NonNull String message) {
        if (developerMode) {
            Log.d(tag, message);
        }
    }

    public static void e(@NonNull String message, @NonNull Throwable throwable) {
        e(TAG, message, throwable);
    }

    public static void e(@NonNull Throwable throwable) {
        e(TAG, throwable.getMessage(), throwable);
    }

    public static void i(@NonNull String message) {
        i(TAG, message);
    }

    public static void d(@NonNull String message) {
        d(TAG, message);
    }

    public static void i(@Nullable Object logSender, @NonNull String message) {
        String senderName = logSender != null ? String.format("[%s]:", logSender.getClass().getSimpleName()) : "";
        i(TAG, senderName + message);
    }

    public static void d(@Nullable Object logSender, @NonNull String message) {
        String senderName = logSender != null ? String.format("[%s]:", logSender.getClass().getSimpleName()) : "";
        d(TAG, senderName + message);
    }
}
