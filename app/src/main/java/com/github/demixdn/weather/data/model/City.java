package com.github.demixdn.weather.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.IOException;
import java.util.List;

/**
 * Created on 07.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
@JsonObject
public final class City {
    @JsonField(name = "name")
    public String name;
    @JsonField(name = "country")
    public String country;

    public static List<City> parseJson(@Nullable String json) throws IOException {
        return LoganSquare.parseList(json, City.class);
    }

    public City() {
    }

    public City(@NonNull String sourceForSplit) {
        String[] split = sourceForSplit.split(",");
        name = split[0];
        if (split.length > 1) {
            country = split[1].trim();
        }
    }

    public String toAppString() {
        return name + ", " + country.toUpperCase();
    }

    public String toQueryString() {
        if (TextUtils.isEmpty(country)) {
            return name;
        }
        return name + "," + country.toLowerCase();
    }

    @Override
    public String toString() {
        return "City{ '" + name + "'; '" + country + "\'}";
    }
}
