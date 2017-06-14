package com.github.demixdn.weather.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.github.demixdn.weather.data.parser.JsonParser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created on 07.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
@JsonObject
public final class City implements Comparable<City>, Serializable {
    @JsonField(name = "name")
    public String name;
    @JsonField(name = "country")
    public String country;

    public float lon;
    public float lat;

    @NonNull
    public static List<City> parseJson(@Nullable String json) throws IOException {
        return JsonParser.parseCitiesJson(json);
    }

    public City() {
    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public City(@NonNull String sourceForSplit) {
        String[] split = sourceForSplit.split(",");
        name = split[0];
        if (split.length > 1) {
            country = split[1].trim();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String toDatabaseKey() {
        return toQueryString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (!name.equals(city.name)) return false;
        return country != null ? country.equals(city.country) : city.country == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{ '" + name + "'; '" + country + "\'}";
    }

    @Override
    public int compareTo(@NonNull City o) {
        return name.compareToIgnoreCase(o.name);
    }
}
