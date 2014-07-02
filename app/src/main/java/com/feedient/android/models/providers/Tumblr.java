package com.feedient.android.models.providers;

import com.feedient.android.interfaces.IProviderModel;

public class Tumblr implements IProviderModel {
    private static final String TEXT_COLOR = "#35465c";
    private static final String ICON = "fa-tumblr";

    public Tumblr() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }
}
