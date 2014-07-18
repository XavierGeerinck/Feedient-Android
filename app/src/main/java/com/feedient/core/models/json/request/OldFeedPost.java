package com.feedient.core.models.json.request;

import org.json.JSONException;
import org.json.JSONObject;

public class OldFeedPost extends JSONObject {
    public OldFeedPost(String providerId, String until) throws JSONException {
        put("providerId", providerId);
        put("until", until);
    }
}
