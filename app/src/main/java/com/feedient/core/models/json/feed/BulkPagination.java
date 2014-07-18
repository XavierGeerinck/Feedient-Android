package com.feedient.core.models.json.feed;

import com.google.gson.annotations.SerializedName;

public class BulkPagination {
    @SerializedName("providerId")
    private String providerId;

    @SerializedName("since")
    private String since;

    @SerializedName("until")
    private String until;

    public BulkPagination() {
        providerId = "";
        since = "";
        until = "";
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

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}
