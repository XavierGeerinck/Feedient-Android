package com.feedient.android.models.providers;

import com.feedient.android.interfaces.IProviderModel;

public class YouTube implements IProviderModel {
    private static final String TEXT_COLOR = "#b31217";
    private static final String ICON = "fa-youtube-square";

    public YouTube() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }
}
