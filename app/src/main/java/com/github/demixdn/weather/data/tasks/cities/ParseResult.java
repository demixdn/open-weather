package com.github.demixdn.weather.data.tasks.cities;

import java.util.List;

/**
 * Created on 07.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class ParseResult {

    public long runDuration;
    public List<String> cities;

    public ParseResult(long runDuration, List<String> cities) {
        this.runDuration = runDuration;
        this.cities = cities;
    }
}
