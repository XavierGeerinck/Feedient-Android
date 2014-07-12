package com.feedient.oauth.interfaces;

import android.content.Context;

import com.feedient.android.interfaces.IAddProviderCallback;

public interface IOAuth1Provider {
    public String getOauthUrl();
    public String getOauthCallbackUrl();
    public String getAppId();

    public void popup(Context context, final String accessToken, IAddProviderCallback callback);
    public void getRequestToken(IGetRequestTokenCallback callback);
}
