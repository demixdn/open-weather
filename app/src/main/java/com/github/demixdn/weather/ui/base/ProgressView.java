package com.github.demixdn.weather.ui.base;

public interface ProgressView {

    void showProgress();

    void hideProgress();

    void showError(String errorMessage);
}
