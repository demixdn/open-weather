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
public class CloudsDTO {
    @JsonField(name = "all")
    public int all;

}
