package com.feedient.core.model;

import com.feedient.core.models.json.UserProvider;

public class User {
    private int id;
    private String email;
    private String language;
    private String role;
    private Workspace[] workspaces;
    private UserProvider[] userProviders;

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

    public Workspace[] getWorkspaces() {
        return workspaces;
    }

    public UserProvider[] getUserProviders() {
        return userProviders;
    }
}
