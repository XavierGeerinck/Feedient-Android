package com.feedient.android.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class FeedPost {
    @SerializedName("id")
    private String id;

    @SerializedName("post_link")
    private String postLink;

    @SerializedName("user")
    private User user;

    @SerializedName("provider")
    private Provider provider;

    @SerializedName("content")
    private Content content;

    @SerializedName("entities")
    private Entities entities;

    @SerializedName("twitter")
    private Twitter twitter;

    @SerializedName("pagination")
    private Pagination pagination;

    public FeedPost() {
        id = "";
        postLink = "";
        user = new User();
        content = new Content();
        twitter = new Twitter();
        provider = new Provider();
        pagination = new Pagination();
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedPost feedPost = (FeedPost) o;

        if (feedPost.getId().equals(((FeedPost) o).getId())) return true;

        return false;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (postLink != null ? postLink.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (provider != null ? provider.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (twitter != null ? twitter.hashCode() : 0);
        return result;
    }
}
