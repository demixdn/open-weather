package com.github.demixdn.weather.data.model.weatherdto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created on 09.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */
@JsonObject
public class WeatherResponseDTO {

    @JsonField(name = "weather")
    public List<ConditionDTO> conditionList;

    @JsonField(name = "main")
    public MainDTO mainInfo;

    @JsonField(name = "wind")
    public WindDTO wind;

    @JsonField(name = "clouds")
    public CloudsDTO clouds;

    @JsonField(name = "dt")
    public  int dateInSeconds;

    @JsonField(name = "sys")
    public SystemDTO system;

    @JsonField(name = "id")
    public int cityId;

    @JsonField(name = "name")
    public String cityName;

    @JsonField(name = "cod")
    public int responseCode;
}
