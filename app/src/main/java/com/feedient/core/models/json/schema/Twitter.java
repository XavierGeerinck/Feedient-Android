package com.feedient.core.models.json.schema;

import com.google.gson.annotations.SerializedName;

public class Twitter {
    @SerializedName("in_reply_to_status_id_str")
    private String inReplyToStatusIdStr;

    public Twitter() {
        inReplyToStatusIdStr = "";
    }

    public String getInReplyToStatusIdStr() {
        return inReplyToStatusIdStr;
    }

    public void setInReplyToStatusIdStr(String inReplyToStatusIdStr) {
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
    }
}
