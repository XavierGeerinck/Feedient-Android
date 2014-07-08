package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.AddProvider;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.oauth.OAuthDialog;
import com.feedient.oauth.interfaces.IOAuth2Provider;
import com.feedient.oauth.webview.WebViewCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

    public void addProvider(String accessToken, FeedientService feedientService, String oAuthCode) {
        feedientService.addOAuth2Provider(accessToken, NAME, oAuthCode, new Callback<AddProvider>() {
            @Override
            public void success(AddProvider addProvider, Response response) {
                Log.e("Feedient", "isSuccess: " + addProvider.isSuccess());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void popup(Context context, final String accessToken) {
        // Create + open the OAuthDialog
        OAuthDialog dialog = new OAuthDialog(context, OAUTH_URL, OAUTH_CALLBACK_URL, new WebViewCallback() {
            @Override
            public void onGotTokens(Dialog oAuthDialog, HashMap<String, String> tokens) {
                addProvider(accessToken, feedientService, tokens.get("code"));

                // close dialogs
                oAuthDialog.dismiss();
            }
        });

        dialog.setTitle("Add Provider");
        dialog.show();
    }
}