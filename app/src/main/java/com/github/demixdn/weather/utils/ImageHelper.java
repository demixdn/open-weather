package com.github.demixdn.weather.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.ui.transformation.CropCircleTransformation;

/**
 * Created on 13.06.2017
 * Project open-weather
 *
 * @author Aleks Sander
 */

public final class ImageHelper {
    private ImageHelper() {
        //no instance
    }

    public static void loadUserPhoto(Context context, int size, Uri userPhotoUrl, ImageView ivUser) {
        Glide.with(context)
                .load(userPhotoUrl)
                .override(size, size)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivUser);
    }
}
