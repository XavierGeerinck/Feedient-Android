package com.feedient.oauth.interfaces;

import com.feedient.core.interfaces.IAddProviderCallback;

public interface IOAuth2Provider {
    public String getOauthUrl();
    public String getOauthCallbackUrl();
    public String getAppId();

    public void popup(final String accessToken, IAddProviderCallback callback);
}
