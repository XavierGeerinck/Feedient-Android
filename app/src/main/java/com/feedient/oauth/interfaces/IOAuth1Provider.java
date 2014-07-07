package com.feedient.oauth.interfaces;

import android.content.Context;

import com.feedient.android.interfaces.FeedientService;

public interface IOAuth1Provider {
    public String[] getOauthFragments();
    public String getOauthUrl();
    public String getOauthCallbackUrl();
    public String getAppId();

    public void popup(Context context, final String accessToken);
    public void getRequestToken(IGetRequestTokenCallback callback);
}
