package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class HashtagEntity {
    @SerializedName("name")
    private String name;

    @SerializedName("link")
    private String link;

    public HashtagEntity() {
        this.name = "";
        this.link = "";
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
