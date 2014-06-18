package com.feedient.adapters;

import android.content.Context;
import com.feedient.adapters.retrofit.ISODateAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.util.Date;
import java.util.Properties;

public class FeedientRestAdapter {
    private Context context;
    private FeedientService feedientService;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;

    public FeedientRestAdapter(Context context) {
        this.context = context;
        assetsPropertyReader = new AssetsPropertyReader(context);
        properties = assetsPropertyReader.getProperties("config.properties");

        // Custom GSON Converter to accept javascript dates
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))   // Apply the new converter
                .setEndpoint(properties.getProperty("api_server.url")) // The base API endpoint
                .setLogLevel(Boolean.parseBoolean(properties.getProperty("api_server.debug")) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        feedientService = restAdapter.create(FeedientService.class);
    }

    public FeedientService getService() {
        return feedientService;
    }
}
