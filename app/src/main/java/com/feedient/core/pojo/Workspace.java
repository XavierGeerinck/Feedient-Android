package com.feedient.core.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workspace implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("creator")
    private String creator;

    @SerializedName("users")
    private List<String> users;

    @SerializedName("date")
    private Date date;

    public Workspace() {
        this.name = "";
        this.creator = "";
        this.users = new ArrayList<String>();
        this.date = new Date();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public List<String> getUsers() { return users; }
    public void setUsers(List<String> users) { this.users = users; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
