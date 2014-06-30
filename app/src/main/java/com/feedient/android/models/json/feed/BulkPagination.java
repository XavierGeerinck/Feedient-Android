package com.feedient.android.models.json.feed;

import com.google.gson.annotations.SerializedName;

public class BulkPagination {
    @SerializedName("providerId")
    private String providerId;

    @SerializedName("since")
    private String since;

    public BulkPagination() {
        providerId = "";
        since = "";
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
