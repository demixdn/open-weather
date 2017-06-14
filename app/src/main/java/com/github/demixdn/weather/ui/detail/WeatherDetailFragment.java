package com.github.demixdn.weather.ui.detail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherDetailFragment extends Fragment {

    private static final String EXTRA_WEATHER_ITEM = "extra_weather_item";

    public static WeatherDetailFragment newInstance(@NonNull Weather weather) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_WEATHER_ITEM, weather);
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Weather weatherItem = null;
        if (getArguments() != null && getArguments().containsKey(EXTRA_WEATHER_ITEM)) {
            weatherItem = (Weather) getArguments().getSerializable(EXTRA_WEATHER_ITEM);
        }
        WeatherViewDelegate weatherViewDelegate = new WeatherViewDelegate(view);
        if (weatherItem != null) {
            weatherViewDelegate.applyItem(weatherItem);
        }
    }

}
