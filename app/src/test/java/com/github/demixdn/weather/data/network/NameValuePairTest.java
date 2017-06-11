package com.github.demixdn.weather.data.network;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */
public class NameValuePairTest {
    private NameValuePair pair;
    @Before
    public void setUp() throws Exception {
        pair = new NameValuePair("Test Name","Test value");
    }

    @Test
    public void getName_Success() throws Exception {
        assertThat(pair.getName(), is("Test Name"));
    }

    @Test
    public void getValue_Success() throws Exception {
        assertThat(pair.getValue(), is("Test value"));
    }

}