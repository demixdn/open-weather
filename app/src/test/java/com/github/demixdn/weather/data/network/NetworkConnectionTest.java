package com.github.demixdn.weather.data.network;

import com.github.demixdn.weather.data.model.JsonParser;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.data.model.WeatherMapper;
import com.github.demixdn.weather.data.model.weatherdto.WeatherResponseDTO;

import org.junit.Before;
import org.junit.Test;

import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.QUERY;
import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.UNIT_METRICS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */
public class NetworkConnectionTest {

    private NetworkConnection connection;

    @Before
    public void setUp(){
        connection = new NetworkConnection();
    }

    @Test
    public void getWeatherByCity_responseNotNull() throws Exception {
        String jsonResponse = connection.getWeatherByCity("Kiev", QUERY, UNIT_METRICS, "ru");

        assertThat(jsonResponse, notNullValue());

    }

    @Test
    public void getWeatherByCity_cityKiev() throws Exception{
        String jsonResponse = connection.getWeatherByCity("Kiev,ua", QUERY, UNIT_METRICS, "ru");

        WeatherResponseDTO weatherResponse = JsonParser.parseWeatherJson(jsonResponse);
        Weather weather = WeatherMapper.transformFrom(weatherResponse);

        assertThat(weather, notNullValue());
        assert weather != null;
        assertThat(weather.getCity().name, is("Kiev"));
        assertThat(weather.getCity().country, is("UA"));
    }

}