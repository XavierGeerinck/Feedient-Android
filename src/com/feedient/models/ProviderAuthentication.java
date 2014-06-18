package com.feedient.models;

import com.google.gson.annotations.SerializedName;

public class ProviderAuthentication {
    @SerializedName("access_token")
    private String accessToken;

    public ProviderAuthentication() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
