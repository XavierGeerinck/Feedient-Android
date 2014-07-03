package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Instagram implements IProviderModel {
    public static final String TEXT_COLOR = "#3f729b";
    public static final String ICON = "fa-instagram";
    public static final String APP_ID = "23320322e2744f9a85f30e807b8f860b";

    public Instagram() {

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