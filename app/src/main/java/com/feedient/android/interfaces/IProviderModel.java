package com.feedient.android.interfaces;

import android.content.Context;

import com.feedient.oauth.interfaces.IOAuth1Provider;
import com.feedient.oauth.interfaces.IOAuth2Provider;

import org.json.JSONException;
import org.json.JSONObject;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();
    public String getName();

    public void popup(Context context, final String accessToken);
}
