package com.feedient.android.models.providers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.interfaces.ISocialActionCallback;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.response.PerformAction;
import com.feedient.android.models.json.schema.FeedPost;
import com.feedient.oauth.OAuthDialog;
import com.feedient.android.interfaces.IAddProviderCallback;
import com.feedient.oauth.models.GetRequestToken;
import com.feedient.oauth.interfaces.IGetRequestTokenCallback;
import com.feedient.oauth.interfaces.IOAuth1Provider;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private List<ProviderAction> actions;

    public Twitter(Context context, FeedientService feedientService, String accessToken) {
        this.accessToken = accessToken;
        this.feedientService = feedientService;
        this.context = context;
        this.actions = new ArrayList<ProviderAction>();

        _initActions();
    }

    private void _initActions() {
        actions.add(new ProviderAction("favorite", "favorited", "{fa-star}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(FeedPost feedPost) {
                if (!feedPost.getContent().getActionsPerformed().isFavorited()) {
                    _doActionFavorite(feedPost);
                } else {
                    _doActionUnFavorite(feedPost);
                }
            }
        }));

        actions.add(new ProviderAction("retweet", "retweeted", "{fa-retweet}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(FeedPost feedPost) {
                if (!feedPost.getContent().getActionsPerformed().isRetweeted()) {
                    _doActionRetweet(feedPost);
                } else {
                    _doActionUnRetweet(feedPost);
                }
            }
        }));
    }

    private void _doActionFavorite(final FeedPost feedPost) {
        feedientService.doActionFavorite(accessToken, feedPost.getProvider().getId(), "favorite", feedPost.getId(), new Callback<PerformAction>() {
            @Override
            public void success(PerformAction performAction, Response response) {
                feedPost.getContent().getActionsPerformed().setFavorited(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    private void _doActionUnFavorite(final FeedPost feedPost) {
        feedientService.undoActionFavorite(accessToken, feedPost.getProvider().getId(), "unfavorite", feedPost.getId(), new Callback<PerformAction>() {
            @Override
            public void success(PerformAction performAction, Response response) {
                feedPost.getContent().getActionsPerformed().setFavorited(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    private void _doActionRetweet(final FeedPost feedPost) {
        feedientService.doActionRetweet(accessToken, feedPost.getProvider().getId(), "retweet", feedPost.getId(), new Callback<PerformAction>() {
            @Override
            public void success(PerformAction performAction, Response response) {
                feedPost.getContent().getActionsPerformed().setRetweeted(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    private void _doActionUnRetweet(final FeedPost feedPost) {
        feedientService.undoActionRetweet(accessToken, feedPost.getProvider().getId(), "delete_retweet", feedPost.getId(), new Callback<PerformAction>() {
            @Override
            public void success(PerformAction performAction, Response response) {
                feedPost.getContent().getActionsPerformed().setRetweeted(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
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
