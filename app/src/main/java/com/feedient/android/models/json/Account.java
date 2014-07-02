package com.feedient.android.models.json;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("language")
    private String language;

    @SerializedName("role")
    private String role;

    public Account() {
        this.id = "";
        this.email = "";
        this.language = "";
        this.role = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
