package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Twitter implements IProviderModel {
    public static final String NAME = "instagram";
    public static final String TEXT_COLOR = "#55acee";
    public static final String ICON = "fa-twitter";
    public static final String APP_ID = "D3VvaK6rZpJ43H3Wreirg";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/twitter";
    public static final String OAUTH_URL = "https://api.twitter.com/oauth/authorize?oauth_token="; //@todo: Needs Request Token
    public static final String[] OAUTH_FRAGMENTS = { "oauth_token", "oauth_verifier" };

    public Twitter() {

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
