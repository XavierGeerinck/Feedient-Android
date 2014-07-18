package com.feedient.core.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class MentionEntity {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id; // Can be empty

    @SerializedName("profile_link")
    private String profileLink;

    public MentionEntity() {
        this.name = "";
        this.id = "";
        this.profileLink = "";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProfileLink() {
        return profileLink;
    }
}
