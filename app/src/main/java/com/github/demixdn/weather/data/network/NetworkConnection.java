package com.github.demixdn.weather.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.github.demixdn.weather.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11.06.2017 in open-weather
 *
 * @author Aleksandr
 */

public class NetworkConnection {
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final int CONNECT_TIMEOUT = 10000;//millis
    private static final int READ_TIMEOUT = 10000;//millis

    @WorkerThread
    @Nullable
    public String getWeatherByCity(@NonNull String cityName, @NonNull String appId,
                                   @NonNull String units, @Nullable String lang)
            throws IOException, URISyntaxException, NetworkException {
        URL targetUrl = getWeatherUrl(cityName, appId, units, lang);
        HttpURLConnection urlConnection = createGetConnection(targetUrl);
        try {
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return IOUtils.getStringFrom(inputStream, IOUtils.ENCODING_UTF_8);
            } else {
                throw new NetworkException(urlConnection.getResponseCode());
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @NonNull
    private URL getWeatherUrl(@NonNull String cityName, @NonNull String appId,
                              @NonNull String units, @Nullable String lang)
            throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
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
    private String getQuery(@NonNull List<NameValuePair> params)
            throws UnsupportedEncodingException {
        if (params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), CHARSET_UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), CHARSET_UTF_8));
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
