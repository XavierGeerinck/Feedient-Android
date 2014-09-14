package com.feedient.core.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Account {
    @SerializedName("_id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("language")
    public String language;

    @SerializedName("role")
    public String role;

    public Account() {
        this.id = "";
        this.email = "";
        this.language = "";
        this.role = "";
    }
}
