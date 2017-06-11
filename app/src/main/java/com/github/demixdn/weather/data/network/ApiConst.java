package com.github.demixdn.weather.data.network;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */

public final class ApiConst {
    static final class URLS {
        static final String BASE_URL = "http://api.openweathermap.org";
        static final String VERSION = "data/2.5";
        static final String WEATHER = "weather";
    }

    static final class PARAM_KEY {
        static final String CITY = "q";
        static final String APP_ID = "appid";
        static final String UNITS = "units";
        static final String LANG = "lang";

    }

    public static final class PARAM_VALUE {
        public static final String APPID = "1f0402f014d6e14ae3fe88f3b8971a82";
        public static final String UNIT_METRICS = "metric";
    }


}
