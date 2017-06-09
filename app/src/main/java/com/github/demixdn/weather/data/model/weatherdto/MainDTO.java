package com.github.demixdn.weather.data.model.weatherdto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
@JsonObject
public class MainDTO {
    @JsonField(name = "temp")
    public double temp;
    @JsonField(name = "temp_min")
    public double tempMin;
    @JsonField(name = "temp_max")
    public double tempMax;
    @JsonField(name = "pressure")
    public double pressure;
    @JsonField(name = "sea_level")
    public double seaLevel;
    @JsonField(name = "grnd_level")
    public double grndLevel;
    @JsonField(name = "humidity")
    public int humidity;
}
