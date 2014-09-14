package com.feedient.core.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workspace implements Serializable {
    @SerializedName("name")
    public String name;

    @SerializedName("creator")
    public String creator;

    @SerializedName("users")
    public List<String> users;

    @SerializedName("date")
    public Date date;

    public Workspace() {
        this.name = "";
        this.creator = "";
        this.users = new ArrayList<String>();
        this.date = new Date();
    }
}
