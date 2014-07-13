package com.feedient.android.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class ActionsPerformed {
    @SerializedName("retweeted")
    private boolean retweeted;

    @SerializedName("favorited")
    private boolean favorited;

    @SerializedName("shared")
    private boolean shared;

    @SerializedName("liked")
    private boolean liked;

    public ActionsPerformed() {
        retweeted = false;
        favorited = false;
        shared = false;
        liked = false;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
