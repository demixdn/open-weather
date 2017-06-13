package com.github.demixdn.weather.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.github.demixdn.weather.utils.IOUtils;
import com.github.demixdn.weather.utils.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 11.06.2017 in open-weather
 *
 * @author Aleksandr
 */

public class NetworkConnection {
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final int CONNECT_TIMEOUT = 10000;//millis
    private static final int READ_TIMEOUT = 10000;//millis

    private int checkAppIdCount = 0;

    @WorkerThread
    @Nullable
    public String getWeatherByCity(@NonNull String cityName, @NonNull String appId,
                                   @NonNull String units, @Nullable String lang)
            throws IOException, URISyntaxException, NetworkException {
        Logger.d("Param city " + cityName);
        URL targetUrl = getWeatherUrl(cityName, appId, units, lang);
        HttpURLConnection urlConnection = createGetConnection(targetUrl);
        try {
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                checkAppIdCount = 0;
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return IOUtils.getStringFrom(inputStream, IOUtils.ENCODING_UTF_8);
            } else if (responseCode == NetworkException.HTTP_TOO_MANY_REQUESTS) {
                return connectWithReserveAppId(cityName, units, lang, responseCode);
            } else {
                checkAppIdCount = 0;
                throw new NetworkException(responseCode);
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private String connectWithReserveAppId(@NonNull String cityName, @NonNull String units,
                                           @Nullable String lang, int responseCode)
            throws IOException, URISyntaxException, NetworkException {
        if (checkAppIdCount < 4) {
            checkAppIdCount++;
            String appid = ApiConst.IDS[new Random().nextInt(4)];
            return getWeatherByCity(cityName, appid, units, lang);
        } else {
            checkAppIdCount = 0;
            throw new NetworkException(responseCode);
        }
    }

    @NonNull
    private URL getWeatherUrl(@NonNull String cityName, @NonNull String appId,
                              @NonNull String units, @Nullable String lang)
            throws URISyntaxException, MalformedURLException {
        String targetUrl = String.format("%s/%s/%s", ApiConst.URLS.BASE_URL,
                ApiConst.URLS.VERSION, ApiConst.URLS.WEATHER);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair(ApiConst.PARAM_KEY.CITY, cityName));
        params.add(new NameValuePair(ApiConst.PARAM_KEY.APP_ID, appId));
        params.add(new NameValuePair(ApiConst.PARAM_KEY.UNITS, units));
        if (lang != null) {
            params.add(new NameValuePair(ApiConst.PARAM_KEY.LANG, lang));
        }

        URI baseUri = new URI(targetUrl);
        String queries = getQuery(params);
        return new URI(baseUri.getScheme(), baseUri.getAuthority(), baseUri.getPath(), queries, null).toURL();
    }

    @Nullable
    private String getQuery(@NonNull List<NameValuePair> params) {
        if (params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(pair.getName());
            result.append("=");
            result.append(pair.getValue());
        }

        return result.toString();
    }

    @NonNull
    private HttpURLConnection createGetConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setDoInput(true);
        urlConnection.setDoInput(true);
        return urlConnection;
    }
}
