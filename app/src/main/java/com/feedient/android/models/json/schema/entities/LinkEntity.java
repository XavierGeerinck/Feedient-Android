package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class LinkEntity {
    @SerializedName("display_url")
    private String displayUrl;

    @SerializedName("expanded_url")
    private String expandedUrl;

    @SerializedName("shortened_url")
    private String shortenedUrl;

    public LinkEntity() {
        this.displayUrl = "";
        this.expandedUrl = "";
        this.shortenedUrl = "";
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }
}
