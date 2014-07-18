package com.feedient.core.models.json.response;

import com.google.gson.annotations.SerializedName;

public class RemoveUserProvider {
    @SerializedName("success")
    private boolean success;

    public RemoveUserProvider() {
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
