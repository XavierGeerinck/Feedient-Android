package com.feedient.android.models.json.response;

import com.google.gson.annotations.SerializedName;

public class Logout {
    @SerializedName("success")
    private boolean success;

    public Logout() {
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
