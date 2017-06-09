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
public class SystemDTO {
    @JsonField(name = "message")
    public double message;
    @JsonField(name = "country")
    public String country;
    @JsonField(name = "sunrise")
    public int sunrise;
    @JsonField(name = "sunset")
    public int sunset;

}
