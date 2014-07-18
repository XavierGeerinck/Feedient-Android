package com.feedient.core.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class ExtendedLinkEntity {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String imageUrl;

    public ExtendedLinkEntity() {
        this.name = "";
        this.description = "";
        this.url = "";
        this.imageUrl = "";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
