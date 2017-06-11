package com.github.demixdn.weather.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 18.01.2017
 * Time: 17:19
 *
 * @author Aleks Sander
 *         Project payments_mb_android
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class IOUtils {

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final int LARGE_BUFFER_SIZE = 8192;
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final int SMALL_BUFFER_SIZE = 1024;

    private IOUtils() {
        //empty
    }

    @Nullable
    public static String getStringFrom(@Nullable InputStream inputStream) throws IOException {
        try {
            String result = null;
            if (inputStream != null) {
                result = writeStreamToString(inputStream, DEFAULT_BUFFER_SIZE, ENCODING_UTF_8);
            }
            return result;
        } finally {
            closeQuietly(inputStream);
        }
    }

    @Nullable
    public static String getStringFrom(@Nullable InputStream inputStream, @NonNull String encoding) throws IOException {
        try {
            String result = null;
            if (inputStream != null) {
                result = writeStreamToString(inputStream, DEFAULT_BUFFER_SIZE, encoding);
            }
            return result;
        } finally {
            closeQuietly(inputStream);
        }
    }

    @Nullable
    public static String getStringFrom(@Nullable InputStream inputStream,
                                       @IntRange(from = SMALL_BUFFER_SIZE, to = Integer.MAX_VALUE) int bufferSize)
            throws IOException {
        try {
            String result = null;
            if (inputStream != null) {
                result = writeStreamToString(inputStream, bufferSize, ENCODING_UTF_8);
            }
            return result;
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static String writeStreamToString(@NonNull InputStream inputStream,
                                             @IntRange(from = SMALL_BUFFER_SIZE, to = Integer.MAX_VALUE) int bufferSize,
                                             @NonNull String encoding)
            throws IOException {
        String result;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            result = readStreamAndConvert(inputStream, outputStream, bufferSize, encoding);
        } finally {
            closeQuietly(outputStream);
        }
        return result;
    }

    private static String readStreamAndConvert(@NonNull InputStream inputStream,
                                               @NonNull ByteArrayOutputStream outputStream,
                                               @IntRange(from = SMALL_BUFFER_SIZE, to = Integer.MAX_VALUE) int bufferSize,
                                               @NonNull String encoding)
            throws IOException {
        String result;
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        result = outputStream.toString(encoding);
        return result;
    }

    public static void closeQuietly(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (Exception ex) {
            Log.e("IOUtils", ex.getMessage(), ex);
        }
    }
}
