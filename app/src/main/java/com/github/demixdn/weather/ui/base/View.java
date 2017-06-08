package com.github.demixdn.weather.ui.base;

public interface View<P extends BasePresenter> {

    void bindPresenter(P presenter);

    P getPresenter();
}
