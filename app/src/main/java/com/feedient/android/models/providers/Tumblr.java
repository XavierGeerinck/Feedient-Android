package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.UserProvider;
import com.feedient.oauth.OAuthDialog;
import com.feedient.android.interfaces.IAddProviderCallback;
import com.feedient.oauth.interfaces.IGetRequestTokenCallback;
import com.feedient.oauth.interfaces.IOAuth1Provider;
import com.feedient.oauth.models.GetRequestToken;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Tumblr implements IProviderModel, IOAuth1Provider {
    public static final String NAME = "tumblr";
    public static final String TEXT_COLOR = "#35465c";
    public static final String ICON = "fa-tumblr";
    public static final String APP_ID = "0eMZWV63dKEc3ec4ePB5AbOFW9IhYLcR3rFaW4CMe7v5Ai0Qki";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/tumblr";
    public static final String OAUTH_URL = "http://www.tumblr.com/oauth/authorize?oauth_token="; //@todo: Needs Request Token

    private Context context;
    private FeedientService feedientService;
    private String accessToken;
    private List<ProviderAction> actions;

    public Tumblr(Context context, FeedientService feedientService, String accessToken) {
        this.context = context;
        this.feedientService = feedientService;
        this.accessToken = accessToken;
        this.actions = new ArrayList<ProviderAction>();

        _initActions();
    }

    private void _initActions() {
        actions.add(new ProviderAction("like", "liked", "{fa-heart}"));
        actions.add(new ProviderAction("reblog", "reblogged", "{fa-retweet}"));
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
        feedientService.addOAuth1Provider(accessToken, NAME, requestSecret, oAuthToken, oAuthVerifier, new Callback<List<UserProvider>>() {
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

    public List<ProviderAction> getActions() {
        return actions;
    }
}