package com.github.demixdn.weather.data.repository;

import java.util.List;

/**
 * Created on 07.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

final class ParseResult {

    private long runDuration;
    public List<String> cities;

    ParseResult(long runDuration, List<String> cities) {
        this.runDuration = runDuration;
        this.cities = cities;
    }
}
