package com.feedient.models.feed;

import com.google.gson.annotations.SerializedName;

public class FeedPost {
    @SerializedName("id")
    private String id;

    @SerializedName("post_link")
    private String postLink;

    @SerializedName("user")
    private User user;

    @SerializedName("content")
    private Content content;

    @SerializedName("twitter")
    private Twitter twitter;

    public FeedPost() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}
