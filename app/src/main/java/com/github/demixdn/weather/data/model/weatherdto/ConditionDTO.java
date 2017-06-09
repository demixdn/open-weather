package com.github.demixdn.weather.data.model.weatherdto;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
@JsonObject
public class ConditionDTO {
    public int id;
    public String main;
    public String description;
    public String icon;

}
