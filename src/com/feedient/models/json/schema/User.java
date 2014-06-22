package com.feedient.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("name_formatted")
    private String nameFormatted;

    @SerializedName("image")
    private String imageLink;

    @SerializedName("profile_link")
    private String profileLink;

    public User() {
        id = "";
        name = "";
        nameFormatted = "";
        imageLink = "";
        profileLink = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFormatted() {
        return nameFormatted;
    }

    public void setNameFormatted(String nameFormatted) {
        this.nameFormatted = nameFormatted;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }
}
