package com.feedient.android.models.json.feed;

import com.feedient.android.models.json.schema.FeedPost;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedPostList {
    @SerializedName("pagination")
    private List<BulkPagination> paginations;

    @SerializedName("posts")
    private List<FeedPost> feedPosts;

    public FeedPostList() {
        paginations = new ArrayList<BulkPagination>();
        feedPosts = new ArrayList<FeedPost>();
    }

    public List<BulkPagination> getPaginations() {
        return paginations;
    }

    public void setPaginations(List<BulkPagination> paginations) {
        this.paginations = paginations;
    }

    public List<FeedPost> getFeedPosts() {
        return feedPosts;
    }

    public void setFeedPosts(List<FeedPost> feedPosts) {
        this.feedPosts = feedPosts;
    }
}
