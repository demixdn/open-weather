package com.github.demixdn.weather.data.network;

/**
 * Created on 11.06.2017 in open-weather.
 *
 * @author Aleksandr
 */

public final class NetworkException extends Exception {

    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_CLIENT_TIMEOUT = 408;
    static final int HTTP_TOO_MANY_REQUESTS = 429;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final int HTTP_BAD_GATEWAY = 502;
    private static final int HTTP_UNAVAILABLE = 503;
    private static final int HTTP_GATEWAY_TIMEOUT = 504;

    private final int errorCode;

    NetworkException(int responseCode) {
        super(getErrorMessage(responseCode));
        this.errorCode = responseCode;
    }

    private static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case HTTP_BAD_REQUEST:
                return "HTTP Status-Code 400: Bad Request";
            case HTTP_UNAUTHORIZED:
                return "HTTP Status-Code 401: Unauthorized";
            case HTTP_FORBIDDEN:
                return "HTTP Status-Code 403: Forbidden";
            case HTTP_NOT_FOUND:
                return "HTTP Status-Code 404: Not Found";
            case HTTP_CLIENT_TIMEOUT:
                return "HTTP Status-Code 408: Request Time-Out";
            case HTTP_TOO_MANY_REQUESTS:
                return "HTTP Status-Code 429: Too Many Requests";
            case HTTP_INTERNAL_ERROR:
                return "HTTP Status-Code 500: Internal Server Error";
            case HTTP_BAD_GATEWAY:
                return "HTTP Status-Code 502: Bad Gateway";
            case HTTP_UNAVAILABLE:
                return "HTTP Status-Code 503: Service Unavailable";
            case HTTP_GATEWAY_TIMEOUT:
                return "HTTP Status-Code 504: Gateway Timeout";
            default:
                return "Status-Code undefined";
        }
    }


}
