package com.feedient.core.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("since")
    private String since;

    public Pagination() {
        since = "";
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }
}
