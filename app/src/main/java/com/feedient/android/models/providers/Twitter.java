package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.response.AddProvider;
import com.feedient.oauth.OAuthDialog;
import com.feedient.oauth.interfaces.IAddProviderCallback;
import com.feedient.oauth.models.GetRequestToken;
import com.feedient.oauth.interfaces.IGetRequestTokenCallback;
import com.feedient.oauth.interfaces.IOAuth1Provider;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Twitter implements IProviderModel, IOAuth1Provider {
    public static final String NAME = "twitter";
    public static final String TEXT_COLOR = "#55acee";
    public static final String ICON = "fa-twitter";
    public static final String APP_ID = "D3VvaK6rZpJ43H3Wreirg";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/twitter";
    public static final String OAUTH_URL = "https://api.twitter.com/oauth/authorize?oauth_token=";

    private FeedientService feedientService;
    private Context context;
    private String accessToken;

    public Twitter(Context context, FeedientService feedientService, String accessToken) {
        this.accessToken = accessToken;
        this.feedientService = feedientService;
        this.context = context;
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

    public void addProvider(String accessToken, FeedientService feedientService, String requestSecret, String oAuthToken, String oAuthVerifier, final IAddProviderCallback callback) {
        feedientService.addOAuth1Provider(accessToken, NAME, requestSecret, oAuthToken, oAuthVerifier, new Callback<UserProvider>() {
            @Override
            public void success(UserProvider userProvider, Response response) {
                callback.onSuccess(userProvider);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void popup(final Context context, final String accessToken, final IAddProviderCallback callback) {
        getRequestToken(new IGetRequestTokenCallback() {
            @Override
            public void success(final GetRequestToken requestToken) {
                // Create + open the OAuthDialog
                OAuthDialog dialog = new OAuthDialog(context, OAUTH_URL + requestToken.getoAuthToken(), OAUTH_CALLBACK_URL, new WebViewCallback() {
                    @Override
                    public void onGotTokens(Dialog oAuthDialog, HashMap<String, String> tokens) {
                        addProvider(accessToken, feedientService, requestToken.getoAuthSecret(), tokens.get("oauth_token"), tokens.get("oauth_verifier"), callback);

                        // close dialogs
                        oAuthDialog.dismiss();
                    }
                });

                dialog.setTitle("Add Provider");
                dialog.show();
            }
        });

    }

    @Override
    public void getRequestToken(final IGetRequestTokenCallback callback) {
        feedientService.getRequestToken(accessToken, NAME, new Callback<GetRequestToken>() {
            @Override
            public void success(GetRequestToken getRequestToken, Response response) {
                // Got request token, call callback for popup
                callback.success(getRequestToken);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
