package com.feedient.android.models.providers;

import android.content.Context;

import com.feedient.android.activities.OAuthActivity;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Twitter implements IProviderModel {
    public static final String NAME = "twitter";
    public static final String TEXT_COLOR = "#55acee";
    public static final String ICON = "fa-twitter";
    public static final String APP_ID = "D3VvaK6rZpJ43H3Wreirg";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/twitter";
    public static final String OAUTH_URL = "https://api.twitter.com/oauth/authorize?oauth_token="; //@todo: Needs Request Token
    public static final String[] OAUTH_FRAGMENTS = { "oauth_token", "oauth_verifier" };

    public Twitter() {

    }

    @Override
    public String getTextColor() {
        return TEXT_COLOR;
    }

    @Override
    public String getIcon() {
        return ICON;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getOauthActivityClass() {
        return OAuthActivity.class;
    }

    @Override
    public String getAppId() {
        return APP_ID;
    }

    @Override
    public String getOauthCallbackUrl() {
        return OAUTH_CALLBACK_URL;
    }

    @Override
    public String getOauthUrl() {
        return OAUTH_URL;
    }

    @Override
    public String[] getOauthFragments() {
        return OAUTH_FRAGMENTS;
    }

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

    @Override
    public void popup(Context context, final FeedientService feedientService, String accessToken) {

    }
}
