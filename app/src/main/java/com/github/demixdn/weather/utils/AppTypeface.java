package com.github.demixdn.weather.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"unused","WeakerAccess"})
public final class AppTypeface {

    @StringDef({Roboto.REGULAR, Roboto.MEDIUM})
    @Retention(RetentionPolicy.SOURCE)
    @interface Font {
    }

    private final Map<String, Typeface> typefaceHashMap;

    private final WeakReference<Context> context;

    public AppTypeface(@NonNull Context context) {
        this.context = new WeakReference<>(context);
        this.typefaceHashMap = new HashMap<>();
    }

    /**
     * HashMap usage for storage typeface. Clear that in clear();
     *
     * @param fontName - Must be one of {@link Roboto}.
     * @return {@link Typeface} from assets by 'fontName' name.
     * Default return Roboto-Regular
     */
    @Nullable
    public final Typeface get(@NonNull @Font String fontName) {
        return getTypeFace(context.get(), fontName);
    }

    public final void setTypefaceIn(@NonNull @Font String typeface, @NonNull TextView in) {
        Typeface font = get(typeface);
        if (font != null)
            in.setTypeface(font);
    }

    @Nullable
    private Typeface getTypeFace(@Nullable Context context, @NonNull String type) {
        if (context == null) {
            return null;
        }
        Typeface current = typefaceHashMap.get(type);
        if (current != null) {
            return current;
        }
        return createTypeface(context, type);
    }

    private Typeface createTypeface(@NonNull Context context, @NonNull String type) {
        Typeface current;
        try {
            current = Typeface.createFromAsset(context.getAssets(), type);
            typefaceHashMap.put(type, current);
            return typefaceHashMap.get(type);
        } catch (Exception ignored) {
            return Typeface.createFromAsset(context.getAssets(), Roboto.REGULAR);
        }
    }

    /**
     * Clear typeface storage.
     */
    public final void clear() {
        typefaceHashMap.clear();
    }

    public static final class Roboto {
        public static final String REGULAR = "fonts/Roboto-Regular.ttf";
        public static final String MEDIUM = "fonts/Roboto-Medium.ttf";

        private Roboto() {
            //only private
        }
    }
}
