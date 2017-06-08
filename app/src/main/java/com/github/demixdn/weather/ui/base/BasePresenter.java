package com.github.demixdn.weather.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends View> {

    @Nullable
    private WeakReference<V> viewReference;

    public void bindView(@NonNull V view) {
        this.viewReference = new WeakReference<>(view);
        onViewBound();
    }

    public void unbindView() {
        if (viewReference != null) {
            this.viewReference.clear();
        }
    }

    @Nullable
    public V getView() {
        if (viewReference != null) {
            return this.viewReference.get();
        }
        return null;
    }

    protected void onViewBound(){
        //override this
    }
}
