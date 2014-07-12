package com.feedient.android.interfaces;

import android.content.Context;

public interface IProviderModel {
    public String getTextColor();
    public String getIcon();
    public String getName();

    public void popup(Context context, final String accessToken, IAddProviderCallback callback);
}
