package com.feedient.models.json;

public class UserSession {
    private String uid;
    private String token;

    public UserSession() {
        uid = "";
        token = "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
