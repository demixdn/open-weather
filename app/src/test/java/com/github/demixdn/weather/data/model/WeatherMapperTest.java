package com.github.demixdn.weather.data.model;

import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;
import com.github.demixdn.weather.data.parser.JsonParser;
import com.github.demixdn.weather.data.mapper.WeatherMapper;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */
public class WeatherMapperTest {

    private static final String jsonWeather = "{\"coord\":{\"lon\":30.52,\"lat\":50.43},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":22.32,\"pressure\":1014,\"humidity\":35,\"temp_min\":22,\"temp_max\":23},\"visibility\":10000,\"wind\":{\"speed\":6,\"deg\":360},\"clouds\":{\"all\":0},\"dt\":1497022200,\"sys\":{\"type\":1,\"id\":7358,\"message\":0.002,\"country\":\"UA\",\"sunrise\":1496972823,\"sunset\":1497031678},\"id\":703448,\"name\":\"Kiev\",\"cod\":200}";
    private WeatherResponseDTO weatherResponse;

    @Before
    public void setUp() throws Exception {
        weatherResponse = JsonParser.parseWeatherJson(jsonWeather);
    }

    @Test
    public void transformFrom_allSuccess() throws Exception {
        Weather weather = WeatherMapper.transformFrom(weatherResponse);
        assertThat(weather, notNullValue());
        assert weather != null;

        assertThat(weather.getCity(), notNullValue(City.class));
        assertThat(weather.getCity().name, is("Kiev"));
        assertThat(weather.getCity().country, is("UA"));

        assertThat(weather.getConditionTitle(), is("Clear"));
        assertThat(weather.getConditionDescription(), is("clear sky"));
        assertThat(weather.getConditionIcon(), is("01d"));

        assertThat(weather.getTemp(), is(22.32));
        assertThat(weather.getTempMin(), is(22.0));
        assertThat(weather.getTempMax(), is(23.0));
        assertThat(weather.getPressure(), is(1014.0));
        assertThat(weather.getHumidity(), is(35));

        assertThat(weather.getWindSpeed(), is(6.0));
        assertThat(weather.getWindDegrees(), is(360.0));

        assertThat(weather.getWeatherTime(), is(1497022200L));
    }

}