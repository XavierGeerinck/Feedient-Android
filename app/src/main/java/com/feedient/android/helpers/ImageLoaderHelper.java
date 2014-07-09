package com.feedient.android.helpers;

import android.content.Context;

import com.feedient.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderHelper {
    /**
     * https://github.com/nostra13/Android-Universal-Image-Loader
     *
     * Gets the ImageLoader
     *
     * @param context
     * @return
     */
    public static ImageLoader getImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                // Enable caching
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);

        return ImageLoader.getInstance();
    }
}
