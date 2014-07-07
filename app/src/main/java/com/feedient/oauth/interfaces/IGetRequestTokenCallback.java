package com.feedient.oauth.interfaces;

import com.feedient.oauth.models.GetRequestToken;

public interface IGetRequestTokenCallback {
    public void success(GetRequestToken requestToken);
}
