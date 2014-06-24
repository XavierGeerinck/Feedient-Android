package com.feedient.android.models.json.feed;

import com.feedient.android.models.json.schema.FeedPost;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedResult {
    @SerializedName("provider")
    private String providerId;

    @SerializedName("posts")
    private List<FeedPost> feedPosts;

    public FeedResult() {
        providerId = "";
        feedPosts = new ArrayList<FeedPost>();
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public List<FeedPost> getFeedPosts() {
        return feedPosts;
    }

    public void setFeedPosts(List<FeedPost> feedPosts) {
        this.feedPosts = feedPosts;
    }
}
