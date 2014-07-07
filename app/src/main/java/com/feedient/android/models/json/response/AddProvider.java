package com.feedient.android.models.json.response;

import com.google.gson.annotations.SerializedName;

public class AddProvider {
    @SerializedName("success")
    private boolean success;

    public AddProvider() {
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
