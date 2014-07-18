package com.feedient.core.models.json.schema;

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

    @SerializedName("reblogged")
    private boolean reblogged;

    @SerializedName("disliked")
    private boolean disliked;

    public ActionsPerformed() {
        retweeted = false;
        favorited = false;
        shared = false;
        liked = false;
        reblogged = false;
        disliked = false;
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

    public boolean isReblogged() {
        return reblogged;
    }

    public void setReblogged(boolean reblogged) {
        this.reblogged = reblogged;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public boolean isDisliked() {
        return disliked;
    }
}
