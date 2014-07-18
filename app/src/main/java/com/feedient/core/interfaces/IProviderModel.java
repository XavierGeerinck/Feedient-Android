package com.feedient.core.interfaces;

import com.feedient.core.models.providers.ProviderAction;

import java.util.List;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();
    public String getName();

    public void popup(final String accessToken, IAddProviderCallback callback);
    public List<ProviderAction> getActions();
}
