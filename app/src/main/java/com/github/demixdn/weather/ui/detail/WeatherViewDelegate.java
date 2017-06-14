package com.github.demixdn.weather.ui.detail;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;

import java.text.DecimalFormat;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class WeatherViewDelegate {

    private static final String FORMAT_WIND_SPEED = "###,##0.0#";
    private static final double HPA_IN_MM = 1.3332239d;

    private final Resources resources;
    private final String packageName;

    private final ImageView iconField;
    private final TextView tempField;
    private final TextView tempMinMaxField;
    private final TextView descriptionField;
    private final TextView pressureField;
    private final TextView humidityField;
    private final TextView windSpeedField;


    WeatherViewDelegate(View parentView) {
        resources = parentView.getResources();
        packageName = parentView.getContext().getPackageName();
        iconField = (ImageView) parentView.findViewById(R.id.ivCurrentWeatherIcon);
        tempField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherTemp);
        tempMinMaxField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherTempMinMax);
        descriptionField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherDescription);
        pressureField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherPressure);
        humidityField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherHumidity);
        windSpeedField = (TextView) parentView.findViewById(R.id.tvCurrentWeatherWindSpeed);
    }

    void applyItem(@NonNull Weather item) {
        int iconId = getIconId(item);
        iconField.setImageResource(iconId);

        int tempInt = (int) item.getTemp();
        tempField.setText(getString(R.string.degree_celsius, tempInt));

        int tempMin = (int) item.getTempMin();
        int tempMax = (int) item.getTempMax();
        tempMinMaxField.setText(getString(R.string.temp_min_max, tempMin, tempMax));

        String description = capitalizeDescription(item);
        descriptionField.setText(description);

        int pressure = (int) (item.getPressure() / HPA_IN_MM);
        pressureField.setText(getString(R.string.pressure, pressure));

        humidityField.setText(getString(R.string.humidity, item.getHumidity()));

        DecimalFormat windFormat = new DecimalFormat(FORMAT_WIND_SPEED);

        windSpeedField.setText(getString(R.string.wind, windFormat.format(item.getWindSpeed())));
    }

    @NonNull
    private String capitalizeDescription(@NonNull Weather item) {
        return item.getConditionDescription().substring(0, 1).toUpperCase() + item.getConditionDescription().substring(1);
    }

    private int getIconId(@NonNull Weather item) {
        int iconId = resources.getIdentifier("icon_" + item.getConditionIcon(), "drawable", packageName);
        if (iconId == 0) {
            iconId = resources.getIdentifier("icon_" + item.getConditionIcon().replace('n', 'd'), "drawable", packageName);
            if (iconId == 0) {
                iconId = R.drawable.icon_50d;
            }
        }
        return iconId;
    }

    private String getString(@StringRes int id, Object... args) {
        return resources.getString(id, args);
    }
}
