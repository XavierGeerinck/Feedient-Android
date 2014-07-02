package com.feedient.android.models.providers;

import com.feedient.android.interfaces.IProviderModel;

public class Instagram implements IProviderModel {
    private static final String TEXT_COLOR = "#3f729b";
    private static final String ICON = "fa-instagram";

    public Instagram() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }
}