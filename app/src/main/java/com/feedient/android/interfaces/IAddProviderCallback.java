package com.feedient.android.interfaces;

import com.feedient.android.models.json.UserProvider;

import java.util.List;

public interface IAddProviderCallback {
    public void onSuccess(List<UserProvider> userProviders);
}
