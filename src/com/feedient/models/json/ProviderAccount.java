package com.feedient.models.json;

import com.google.gson.annotations.SerializedName;

public class ProviderAccount {
    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("authentication")
    private ProviderAuthentication providerAuthentication;

    public ProviderAccount() {
        name = "";
        username = "";
        userId = "";
        fullName = "";
        providerAuthentication = new ProviderAuthentication();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ProviderAuthentication getProviderAuthentication() {
        return providerAuthentication;
    }

    public void setProviderAuthentication(ProviderAuthentication providerAuthentication) {
        this.providerAuthentication = providerAuthentication;
    }
}
