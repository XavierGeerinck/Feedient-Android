package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;

import org.json.JSONException;
import org.json.JSONObject;

public class YouTube implements IProviderModel {
    public static final String TEXT_COLOR = "#b31217";
    public static final String ICON = "fa-youtube-square";
    public static final String APP_ID = "1053845138024-cp822342mv7q48kssktq0qk6bk01r3ut.apps.googleusercontent.com";

    public YouTube() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }

    @Override
    public void addProvider(String accessToken, FeedientService feedientService, JSONObject jo) throws JSONException {

    }
}
