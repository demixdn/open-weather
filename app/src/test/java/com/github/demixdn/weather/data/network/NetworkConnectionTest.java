package com.github.demixdn.weather.data.network;

import org.junit.Test;

import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.APPID;
import static com.github.demixdn.weather.data.network.ApiConst.PARAM_VALUE.UNIT_METRICS;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */
public class NetworkConnectionTest {
    @Test
    public void getWeatherByCity_responseNotNull() throws Exception {
        NetworkConnection connection = new NetworkConnection();
        String response = connection.getWeatherByCity("Kiev", APPID, UNIT_METRICS, "ru");

        assertThat(response, notNullValue());
    }

}