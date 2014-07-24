package com.feedient.core.models.json.response;

import com.google.gson.annotations.SerializedName;

public class PostMessage {
    @SerializedName("post_id")
    private String postId;

    @SerializedName("provider")
    private String providerId;

    public PostMessage() {
        this.postId = "";
        this.providerId = "";
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
