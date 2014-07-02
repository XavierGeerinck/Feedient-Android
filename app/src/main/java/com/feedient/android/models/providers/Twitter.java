package com.feedient.android.models.providers;

import com.feedient.android.interfaces.IProviderModel;

public class Twitter implements IProviderModel {
    private static final String TEXT_COLOR = "#55acee";
    private static final String ICON = "fa-twitter";

    public Twitter() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }
}
