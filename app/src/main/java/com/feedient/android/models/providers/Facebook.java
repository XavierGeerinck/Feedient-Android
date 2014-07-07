package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.oauth.OAuthDialog;
import com.feedient.oauth.interfaces.IGetRequestTokenCallback;
import com.feedient.oauth.interfaces.IOAuth2Provider;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Facebook implements IProviderModel, IOAuth2Provider {
    public static final String NAME = "facebook";
    public static final String TEXT_COLOR = "#3b5998";
    public static final String ICON = "fa-facebook-square";
    public static final String APP_ID = "454088611354529";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/facebook";
    public static final String OAUTH_URL = "https://facebook.com/v2.0/dialog/oauth?client_id=" + APP_ID + "&display=popup&scope=read_stream,manage_notifications,publish_actions,publish_stream,user_photos,friends_photos,friends_likes,friends_videos,friends_status,friends_relationship_details,user_photos&redirect_uri=" + OAUTH_CALLBACK_URL;
    public static final String[] OAUTH_FRAGMENTS = { "oauth_code" };

    private Context context;
    private FeedientService feedientService;
    private String accessToken;

    public Facebook(Context context, FeedientService feedientService, String accessToken) {
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

    public void addProvider(String accessToken, FeedientService feedientService, String oAuthCode) {
        feedientService.addProviderFacebook(accessToken, NAME, oAuthCode, new Callback<RemoveUserProvider>() {
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
