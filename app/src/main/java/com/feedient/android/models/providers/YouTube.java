package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class YouTube implements IProviderModel {
    public static final String NAME = "instagram";
    public static final String TEXT_COLOR = "#b31217";
    public static final String ICON = "fa-youtube-square";
    public static final String APP_ID = "1053845138024-cp822342mv7q48kssktq0qk6bk01r3ut.apps.googleusercontent.com";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/youtube";
    public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth?client_id=" + APP_ID + "&response_type=code&scope=https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube&access_type=offline&approval_prompt=force&redirect_uri=" + OAUTH_CALLBACK_URL;
    public static final String[] OAUTH_FRAGMENTS = { "oauth_code" };

    public YouTube() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }

    public String getName() {
        return NAME;
    }

    public String getAppId() {
        return APP_ID;
    }

    public String getOauthCallbackUrl() {
        return OAUTH_CALLBACK_URL;
    }

    public String getOauthUrl() {
        return OAUTH_URL;
    }

    public String[] getOauthFragments() {
        return OAUTH_FRAGMENTS;
    }

    @Override
    public void addProvider(String accessToken, FeedientService feedientService, JSONObject jo) throws JSONException {
        feedientService.addProviderFacebook(accessToken, NAME, jo.getString("oauth_code"), new Callback<RemoveUserProvider>() {
            @Override
            public void success(RemoveUserProvider removeUserProvider, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
