package com.feedient.oauth.models;

import com.google.gson.annotations.SerializedName;

public class GetRequestToken {
    @SerializedName("oauth_token")
    private String oAuthToken;

    @SerializedName("oauth_secret")
    private String oAuthSecret;

    public GetRequestToken() {
        this.oAuthToken = "";
        this.oAuthSecret = "";
    }

    public String getoAuthToken() {
        return oAuthToken;
    }

    public void setoAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public String getoAuthSecret() {
        return oAuthSecret;
    }

    public void setoAuthSecret(String oAuthSecret) {
        this.oAuthSecret = oAuthSecret;
    }
}
