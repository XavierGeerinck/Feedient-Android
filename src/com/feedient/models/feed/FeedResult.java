package com.feedient.models.feed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedResult {
    @SerializedName("provider")
    private String providerId;

    @SerializedName("posts")
    private List<FeedPost> feedPosts;

    public FeedResult() {

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
