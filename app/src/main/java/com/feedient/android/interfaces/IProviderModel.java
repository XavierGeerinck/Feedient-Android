package com.feedient.android.interfaces;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();

    public String[] getOauthFragments();
    public String getOauthUrl();
    public String getOauthCallbackUrl();
    public String getAppId();
    public String getName();

    public Class getOauthActivityClass();

    //public void addProvider(String accessToken, FeedientService feedientService, JSONObject jo) throws JSONException;

    public void popup(Context context, final FeedientService feedientService, String accessToken);
}
