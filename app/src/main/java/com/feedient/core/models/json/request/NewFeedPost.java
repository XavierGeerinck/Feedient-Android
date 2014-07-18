package com.feedient.core.models.json.request;

import org.json.JSONException;
import org.json.JSONObject;

public class NewFeedPost extends JSONObject {
    public NewFeedPost(String providerId, String since) throws JSONException {
        put("providerId", providerId);
        put("since", since);
    }
}
