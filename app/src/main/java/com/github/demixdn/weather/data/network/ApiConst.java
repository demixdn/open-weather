package com.github.demixdn.weather.data.network;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */

public final class ApiConst {
    static final String[] IDS = new String[]{"8fc7118ea2001977dcaecf9bc9c6bb70",
            "17d9d1f6ba2be4ff15f6b8e0b94c845a",
            "ff6f5d189dfa1eba10cc3559a7eb097d",
            "3bb039201af7704f35c2b60bde6663a2"};

    private ApiConst() {
        //no instance
    }

    static final class URLS {
        static final String BASE_URL = "http://api.openweathermap.org";
        static final String VERSION = "data/2.5";
        static final String WEATHER = "weather";

        private URLS() {
            //no instance
        }
    }

    static final class PARAM_KEY {
        static final String CITY = "q";
        static final String APP_ID = "appid";
        static final String UNITS = "units";
        static final String LANG = "lang";

        private PARAM_KEY() {
            //no instance
        }
    }

    public static final class PARAM_VALUE {
        public static final String QUERY = "1f0402f014d6e14ae3fe88f3b8971a82";
        public static final String UNIT_METRICS = "metric";

        private PARAM_VALUE() {
            //no instance
        }
    }


}
