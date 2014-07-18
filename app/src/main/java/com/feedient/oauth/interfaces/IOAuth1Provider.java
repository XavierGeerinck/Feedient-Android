package com.feedient.oauth.interfaces;

import com.feedient.core.interfaces.IAddProviderCallback;

public interface IOAuth1Provider {
    public String getOauthUrl();
    public String getOauthCallbackUrl();
    public String getAppId();

    public void popup(final String accessToken, IAddProviderCallback callback);
    public void getRequestToken(IGetRequestTokenCallback callback);
}
