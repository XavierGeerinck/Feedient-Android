package com.feedient.models.feed;

import com.google.gson.annotations.SerializedName;

public class ActionsPerformed {
    @SerializedName("retweeted")
    private boolean retweeted;

    @SerializedName("favorited")
    private boolean favorited;

    public ActionsPerformed() {
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
