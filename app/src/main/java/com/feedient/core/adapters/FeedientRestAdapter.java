package com.feedient.core.adapters;

import android.content.Context;

import com.feedient.core.adapters.gson.ISODateAdapter;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.api.FeedientService;
import com.google.gson.FieldNamingPolicy;
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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(Date.class, new ISODateAdapter()); // Correct date parsing

        Gson gson = gsonBuilder.create();


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
