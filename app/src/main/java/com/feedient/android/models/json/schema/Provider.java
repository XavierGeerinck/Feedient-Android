package com.feedient.android.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class Provider {
    @SerializedName("id")
    private String id;

    public Provider() {
        id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
