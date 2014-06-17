package com.feedient.adapters;

public class FeedientRestAdapter {
    FeedientRestAdapter restAdapter = new FeedientRestAdapter().RestAdapter.Builder()
            .setServer("https://api.feedient.com") // The base API endpoint
            .build();
}
