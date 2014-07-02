package com.feedient.android.models.providers;

import com.feedient.android.interfaces.IProviderModel;

public class Facebook implements IProviderModel {
    private static final String TEXT_COLOR = "#3b5998";
    private static final String ICON = "fa-facebook-square";

    public Facebook() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }
}
