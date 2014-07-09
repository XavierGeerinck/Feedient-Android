package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class VideoEntity {
    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;

    public VideoEntity() {
        this.name = "";
        this.image = "";
        this.url = "";
        this.description = "";
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
