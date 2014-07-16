package com.feedient.android.interfaces;

import android.content.Context;

import com.feedient.android.models.providers.ProviderAction;

import java.util.List;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();
    public String getName();

    public void popup(final String accessToken, IAddProviderCallback callback);
    public List<ProviderAction> getActions();
}
