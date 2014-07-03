package com.feedient.android.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();

    void addProvider(String accessToken, FeedientService feedientService, JSONObject jo) throws JSONException;
}
