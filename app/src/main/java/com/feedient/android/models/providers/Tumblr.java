package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Tumblr implements IProviderModel {
    public static final String TEXT_COLOR = "#35465c";
    public static final String ICON = "fa-tumblr";
    public static final String APP_ID = "S8NXaMjYSrlDt1hhLCNnw7BucYviEtpz4o5fY26TGkNdVm9aV9";

    public Tumblr() {

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
