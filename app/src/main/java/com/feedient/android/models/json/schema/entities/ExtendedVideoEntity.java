package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class ExtendedVideoEntity {
    @SerializedName("link")
    private String link;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("image")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("duration")
    private String duration;

    public ExtendedVideoEntity() {
        this.link = "";
        this.description = "";
        this.thumbnail = "";
        this.title = "";
        this.duration = "";
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }
}
