package com.github.demixdn.weather.ui.cities;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.model.Weather;

/**
 * Created on 12.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class CityWeatherViewHolder extends RecyclerView.ViewHolder {

    private final View contentView;
    private final TextView cityTitleField;
    private final ImageView weatherIconField;
    private final TextView tempField;
    private final Resources resources;
    private final String packageName;
    private final Button undoButton;
    private final WeatherItemClickListener clickListener;


    CityWeatherViewHolder(ViewGroup parentView, final WeatherItemClickListener clickListener) {
        super(LayoutInflater.from(parentView.getContext()).inflate(R.layout.view_city_item, parentView, false));
        this.clickListener = clickListener;
        resources = itemView.getResources();
        cityTitleField = (TextView) itemView.findViewById(R.id.tvCityItemTitle);
        tempField = (TextView) itemView.findViewById(R.id.tvCityItemTemp);
        weatherIconField = (ImageView) itemView.findViewById(R.id.ivCityItemCondition);
        packageName = itemView.getContext().getPackageName();
        contentView = itemView.findViewById(R.id.llCityContent);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onWeatherItemClicked((Weather) v.getTag());
            }
        });
        undoButton = (Button) itemView.findViewById(R.id.btCityItemRemoveUndo);
    }


    void normalState(@NonNull Weather item) {
        itemView.setBackgroundColor(Color.TRANSPARENT);
        contentView.setVisibility(View.VISIBLE);
        undoButton.setVisibility(View.GONE);
        undoButton.setOnClickListener(null);
        contentView.setTag(item);
        applyItem(item);
    }

    private void applyItem(@NonNull Weather item) {
        cityTitleField.setText(item.getCity().toAppString());
        int tempInt = (int) item.getTemp();
        tempField.setText(resources.getString(R.string.degree_celsius, tempInt));
        int iconId = resources.getIdentifier("icon_" + item.getConditionIcon(), "drawable", packageName);
        if (iconId == 0) {
            iconId = resources.getIdentifier("icon_" + item.getConditionIcon().replace('n', 'd'), "drawable", packageName);
            if (iconId == 0) {
                iconId = R.drawable.icon_50d;
            }
        }
        weatherIconField.setImageResource(iconId);
    }

    void undoState(@NonNull View.OnClickListener clickListener) {
        itemView.setBackgroundColor(Color.RED);
        contentView.setVisibility(View.GONE);
        undoButton.setVisibility(View.VISIBLE);
        undoButton.setOnClickListener(clickListener);
    }
}
