package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.UserProvider;
import com.feedient.oauth.OAuthDialog;
import com.feedient.android.interfaces.IAddProviderCallback;
import com.feedient.oauth.interfaces.IOAuth2Provider;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Instagram implements IProviderModel, IOAuth2Provider {
    public static final String NAME = "instagram";
    public static final String TEXT_COLOR = "#3f729b";
    public static final String ICON = "fa-instagram";
    public static final String APP_ID = "bda95c8497074bc58b84076c8f576e2f";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/instagram";
    public static final String OAUTH_URL = "https://api.instagram.com/oauth/authorize?client_id=" + APP_ID + "&response_type=code&scope=basic+comments+relationships+likes&redirect_uri=" + OAUTH_CALLBACK_URL;

    private FeedientService feedientService;
    private Context context;
    private String accessToken;

    public Instagram(Context context, FeedientService feedientService, String accessToken) {
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

    public void addProvider(String accessToken, FeedientService feedientService, String oAuthCode, final IAddProviderCallback callback) {
        feedientService.addOAuth2Provider(accessToken, NAME, oAuthCode, new Callback<List<UserProvider>>() {
            @Override
            public void success(List<UserProvider> userProviders, Response response) {
                callback.onSuccess(userProviders);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void popup(Context context, final String accessToken, final IAddProviderCallback callback) {
        // Create + open the OAuthDialog
        OAuthDialog dialog = new OAuthDialog(context, OAUTH_URL, OAUTH_CALLBACK_URL, new WebViewCallback() {
            @Override
            public void onGotTokens(Dialog oAuthDialog, HashMap<String, String> tokens) {
                addProvider(accessToken, feedientService, tokens.get("code"), callback);

                // close dialogs
                oAuthDialog.dismiss();
            }
        });

        dialog.setTitle("Add Provider");
        dialog.show();
    }
}