package com.feedient.core.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class Tumblr {
    @SerializedName("reblog_key")
    private String reblogKey;

    @SerializedName("post_type")
    private String postType;

    public Tumblr() {
        this.reblogKey = "";
        this.postType = "";
    }

    public String getReblogKey() {
        return reblogKey;
    }

    public String getPostType() {
        return postType;
    }
}
