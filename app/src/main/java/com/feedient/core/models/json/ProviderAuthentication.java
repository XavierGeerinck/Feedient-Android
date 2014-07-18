package com.feedient.core.models.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProviderAuthentication implements Serializable {
    @SerializedName("access_token")
    private String accessToken;

    public ProviderAuthentication() {
        accessToken = "";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
