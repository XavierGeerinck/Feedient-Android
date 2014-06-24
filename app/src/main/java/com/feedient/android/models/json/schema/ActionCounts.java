package com.feedient.android.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class ActionCounts {
    @SerializedName("retweets")
    private int retweets;

    @SerializedName("favorited")
    private int favorited;

    public ActionCounts() {
        retweets = 0;
        favorited = 0;
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
