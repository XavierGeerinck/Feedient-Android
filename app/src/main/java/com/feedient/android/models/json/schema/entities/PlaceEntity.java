package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class PlaceEntity {
    @SerializedName("url")
    private String url;

    @SerializedName("name")
    private String name;

    public PlaceEntity() {
        this.url = "";
        this.name = "";
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
