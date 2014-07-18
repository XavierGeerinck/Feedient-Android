package com.feedient.core.interfaces;

import com.feedient.core.models.json.UserProvider;

import java.util.List;

public interface IAddProviderCallback {
    public void onSuccess(List<UserProvider> userProviders);
}
