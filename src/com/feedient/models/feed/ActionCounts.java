package com.feedient.models.feed;

import com.google.gson.annotations.SerializedName;

public class ActionCounts {
    @SerializedName("retweets")
    private int retweets;

    @SerializedName("favorited")
    private int favorited;

    public ActionCounts() {
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }
}
