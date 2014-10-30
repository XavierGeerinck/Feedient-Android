package com.feedient.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UserProvider implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("order")
    private int order;

    @SerializedName("date_added")
    private Date dateAdded;

    @SerializedName("provider")
    private String provider;

    public UserProvider() {
    }

    public String getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String getProvider() {
        return provider;
    }
}