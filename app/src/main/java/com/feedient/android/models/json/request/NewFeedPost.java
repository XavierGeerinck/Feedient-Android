package com.feedient.android.models.json.request;

import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

public class NewFeedPost extends JSONObject {
    public NewFeedPost(String providerId, String since) throws JSONException {
        put("providerId", providerId);
        put("since", since);
    }
}
