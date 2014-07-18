package com.feedient.core.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AssetsPropertyReader {
    private Context context;
    private Properties properties;

    public AssetsPropertyReader(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String fileName) {
        try {
            // Gets the assetManager, this provides access to the application's raw asset files
            AssetManager assetManager = context.getAssets();
            // Open a file using ACCESS_STREAMING mode.
            InputStream is = assetManager.open(fileName);
            properties.load(is);
        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.getMessage());
        }

        return properties;
    }
}
