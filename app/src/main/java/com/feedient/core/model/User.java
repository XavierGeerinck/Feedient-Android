package com.feedient.core.model;

import com.feedient.core.models.json.UserProvider;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    private int id;
    private String email;
    private String language;
    private String role;
    private List<Workspace> workspaces;

    @SerializedName("user_providers")
    private List<UserProvider> userProviders;

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLanguage() {
        return language;
    }

    public String getRole() {
        return role;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public List<UserProvider> getUserProviders() {
        return userProviders;
    }
}
