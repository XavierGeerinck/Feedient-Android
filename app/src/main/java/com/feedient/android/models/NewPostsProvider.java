package com.feedient.android.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPostsProvider {
    private String providerId;
    private String since;

    public NewPostsProvider() {
        providerId = "";
        since = "";
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    @Override
    public String toString() {
        JSONObject jo = new JSONObject();

        try {
            jo.put("providerId", providerId);
            jo.put("since", since);
        } catch (JSONException e) {
            return e.getMessage();
        }

        return jo.toString();
    }
}
