package com.feedient.core.models.json.response;

import com.google.gson.annotations.SerializedName;

public class PerformAction {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private String data;

    public PerformAction() {
        this.success = false;
        this.data = "";
    }

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }
}
