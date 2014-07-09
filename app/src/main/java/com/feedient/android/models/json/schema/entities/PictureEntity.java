package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class PictureEntity {
    @SerializedName("small_picture")
    private String smallPictureUrl;

    @SerializedName("large_picture")
    private String largePictureUrl;

    @SerializedName("caption")
    private String caption; // Can be empty

    public PictureEntity() {
        this.smallPictureUrl = "";
        this.largePictureUrl = "";
        this.caption = "";
    }

    public String getSmallPictureUrl() {
        return smallPictureUrl;
    }

    public String getLargePictureUrl() {
        return largePictureUrl;
    }

    public String getCaption() {
        return caption;
    }
}
