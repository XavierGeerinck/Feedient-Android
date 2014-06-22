package com.feedient.models.json.socket;

import com.google.gson.annotations.SerializedName;

public class SocketResponse {
    @SerializedName("type")
    private String type;

    @SerializedName("content")
    private SocketContent content;

    public SocketResponse() {
        type = "";
        content = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SocketContent getContent() {
        return content;
    }

    public void setContent(SocketContent content) {
        this.content = content;
    }
}
