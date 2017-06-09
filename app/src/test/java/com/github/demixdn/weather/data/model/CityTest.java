package com.github.demixdn.weather.data.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 08.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CityTest {
    private static final String jsonCities = "[{\"id\":707860,\"name\":\"Hurzuf\",\"country\":\"UA\"},{\"id\":519188,\"name\":\"Novinki\",\"country\":\"RU\"}]";

    @Test
    public void fromString_nameAndCountry() throws Exception {
        String source = "Hurzuf, UA";
        City city = new City(source);
        assertThat(city.name, is("Hurzuf"));
        assertThat(city.country, is("UA"));
    }

    @Test
    public void fromString_onlyName() throws Exception {
        String source = "Kiev";
        City city = new City(source);
        assertThat(city.name, is("Kiev"));
        assertThat(city.country, nullValue(String.class));
        assertThat(city.toQueryString(), is("Kiev"));
    }

    @Test
    public void forApp_allSuccess() throws Exception {
        List<City> cities = City.parseJson(jsonCities);
        assertThat(cities.get(0).toAppString(), is("Hurzuf, UA"));
        assertThat(cities.get(1).toAppString(), is("Novinki, RU"));
    }

    @Test
    public void forApi_allSuccess() throws Exception {
        List<City> cities = City.parseJson(jsonCities);
        assertThat(cities.get(0).toQueryString(), is("Hurzuf,ua"));
        assertThat(cities.get(1).toQueryString(), is("Novinki,ru"));
    }

}