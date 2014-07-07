package com.feedient.android.models.providers;

import android.content.Context;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.oauth.interfaces.IOAuth2Provider;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class YouTube implements IProviderModel, IOAuth2Provider {
    public static final String NAME = "youtube";
    public static final String TEXT_COLOR = "#b31217";
    public static final String ICON = "fa-youtube-square";
    public static final String APP_ID = "1053845138024-cp822342mv7q48kssktq0qk6bk01r3ut.apps.googleusercontent.com";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/youtube";
    public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth?client_id=" + APP_ID + "&response_type=code&scope=https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube&access_type=offline&approval_prompt=force&redirect_uri=" + OAUTH_CALLBACK_URL;
    public static final String[] OAUTH_FRAGMENTS = { "oauth_code" };

    private Context context;
    private FeedientService feedientService;
    private String accessToken;

    public YouTube(Context context, FeedientService feedientService, String accessToken) {
        this.context = context;
        this.feedientService = feedientService;
        this.accessToken = accessToken;
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

    public void addProvider(String accessToken, JSONObject jo) throws JSONException {
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
    public void popup(Context context, final String accessToken) {

    }
}
