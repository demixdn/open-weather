package com.github.demixdn.weather.data.model;

import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;
import com.github.demixdn.weather.data.parser.JsonParser;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
public class JsonParserTest {
    private static final String jsonCities = "[{\"id\":707860,\"name\":\"Hurzuf\",\"country\":\"UA\"},{\"id\":519188,\"name\":\"Novinki\",\"country\":\"RU\"}]";
    private static final String jsonWeather = "{\"coord\":{\"lon\":30.52,\"lat\":50.43},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":22.32,\"pressure\":1014,\"humidity\":35,\"temp_min\":22,\"temp_max\":23},\"visibility\":10000,\"wind\":{\"speed\":6,\"deg\":360},\"clouds\":{\"all\":0},\"dt\":1497022200,\"sys\":{\"type\":1,\"id\":7358,\"message\":0.002,\"country\":\"UA\",\"sunrise\":1496972823,\"sunset\":1497031678},\"id\":703448,\"name\":\"Kiev\",\"cod\":200}";

    @Test
    public void parseCitiesJson() throws Exception {
        List<City> cities = JsonParser.parseCitiesJson(jsonCities);
        assertThat(cities.get(0).name, is("Hurzuf"));
        assertThat(cities.get(0).country, is("UA"));
        assertThat(cities.get(1).name, is("Novinki"));
        assertThat(cities.get(1).country, is("RU"));
    }

    @Test
    public void parseWeatherJson() throws Exception {
        WeatherResponseDTO response = JsonParser.parseWeatherJson(jsonWeather);

        assertThat(response, notNullValue());
        assert response != null;

        assertThat(response.conditionList, notNullValue());
        assertThat(response.conditionList.get(0), notNullValue());
        assertThat(response.conditionList.get(0).id, is(800));
        assertThat(response.conditionList.get(0).main, is("Clear"));
        assertThat(response.conditionList.get(0).description, is("clear sky"));
        assertThat(response.conditionList.get(0).icon, is("01d"));

        assertThat(response.mainInfo, notNullValue());
        assertThat(response.mainInfo.temp, is(22.32));
        assertThat(response.mainInfo.tempMin, is(22.0));
        assertThat(response.mainInfo.tempMax, is(23.0));
        assertThat(response.mainInfo.pressure, is(1014.0));
        assertThat(response.mainInfo.humidity, is(35));
        assertThat(response.mainInfo.seaLevel, is(0.0));
        assertThat(response.mainInfo.grndLevel, is(0.0));

        assertThat(response.wind, notNullValue());
        assertThat(response.wind.speed, is(6.0));
        assertThat(response.wind.deg, is(360.0));

        assertThat(response.clouds, notNullValue());
        assertThat(response.clouds.all, is(0));

        assertThat(response.system, notNullValue());
        assertThat(response.system.message, is(0.002));
        assertThat(response.system.country, is("UA"));
        assertThat(response.system.sunrise, is(1496972823));
        assertThat(response.system.sunset, is(1497031678));

        assertThat(response.cityId, is(703448));
        assertThat(response.dateInSeconds, is(1497022200));
        assertThat(response.cityName, is("Kiev"));
        assertThat(response.responseCode, is(200));

    }

}