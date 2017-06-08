package com.github.demixdn.weather.ui.addcity;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.demixdn.weather.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

class ApproveCityDelegate implements View.OnClickListener {
    private final LinearLayout parentView;
    private final AddCityPresenter presenter;
    private final Map<String, View> cityViewMap;

    ApproveCityDelegate(@NonNull LinearLayout parentView, @NonNull AddCityPresenter presenter) {
        this.parentView = parentView;
        this.presenter = presenter;
        this.cityViewMap = new HashMap<>();
        restoreSate();
    }

    private void restoreSate() {
        Map<String, Boolean> cachedState = presenter.getCityStatusCache();
        Set<Map.Entry<String, Boolean>> entries = cachedState.entrySet();
        for (Map.Entry<String, Boolean> entry : entries) {
            String cityName = entry.getKey();
            boolean status = entry.getValue();
            addCityView(cityName, status);
        }
    }

    void addCity(@NonNull String cityName) {
        if (cityViewMap.get(cityName) == null) {
            addCityView(cityName, false);
            presenter.addCity(cityName);
        }
    }

    private void addCityView(@NonNull String cityName, boolean status) {
        View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.view_approved_city_item, parentView, false);
        TextView tvCityName = (TextView) view.findViewById(R.id.tvAddedCity);
        tvCityName.setText(cityName);
        View btRemoveCity = view.findViewById(R.id.btCancelCity);
        btRemoveCity.setTag(cityName);
        btRemoveCity.setOnClickListener(this);
        cityViewMap.put(cityName, view);
        setViewStatus(status, view);
        parentView.addView(view);
    }

    void onAddCityApproved(@NonNull String cityName, boolean result) {
        View view = cityViewMap.get(cityName);
        setViewStatus(result, view);
    }

    private void setViewStatus(boolean result, View view) {
        int progressVisibility = result ? View.GONE : View.VISIBLE;
        int approvedVisibility = result ? View.VISIBLE : View.INVISIBLE;
        view.findViewById(R.id.ivAddedCityStatusProgress).setVisibility(progressVisibility);
        view.findViewById(R.id.ivAddedCityStatusApproved).setVisibility(approvedVisibility);
        view.findViewById(R.id.btCancelCity).setVisibility(approvedVisibility);
    }

    void onAddRemoveApproved(@NonNull String cityName, boolean result) {
        if (result) {
            parentView.removeView(cityViewMap.get(cityName));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancelCity:
                String cityName = (String) v.getTag();
                presenter.removeCity(cityName);
                break;
        }
    }
}
