package com.feedient.android.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class NotificationContent {
    @SerializedName("message")
    private String message;

    public NotificationContent() {
        message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
