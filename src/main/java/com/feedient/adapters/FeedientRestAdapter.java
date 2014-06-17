package com.feedient.adapters;

import com.feedient.interfaces.FeedientService;
import retrofit.RestAdapter;

public class FeedientRestAdapter {
    FeedientService feedientService;

    public FeedientRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.feedient.com") // The base API endpoint
                .build();

        feedientService = restAdapter.create(FeedientService.class);
    }

    public FeedientService getService() {
        return feedientService;
    }
}
