package com.feedient.core.models.providers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.feedient.core.interfaces.FeedientService;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.interfaces.ISocialActionCallback;
import com.feedient.core.models.json.UserProvider;
import com.feedient.core.models.json.response.PerformAction;
import com.feedient.core.models.json.schema.FeedPost;
import com.feedient.oauth.OAuthDialog;
import com.feedient.core.interfaces.IAddProviderCallback;
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
import rx.functions.Action1;

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
        actions.add(new ProviderAction("like", "liked", "{fa-heart}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(FeedPost feedPost) {
                if (!feedPost.getContent().getActionsPerformed().isLiked()) {
                    _doActionLike(feedPost);
                } else {
                    _doActionUnlike(feedPost);
                }
            }
        }));

        actions.add(new ProviderAction("reblog", "reblogged", "{fa-retweet}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(FeedPost feedPost) {
                _doActionReblog(feedPost);
            }
        }));
    }

    private void _doActionReblog(final FeedPost feedPost) {
        feedientService.doActionTumblrReblog(accessToken, feedPost.getProvider().getId(), "reblog", feedPost.getId(), feedPost.getTumblr().getReblogKey())
            .subscribe(new Action1<PerformAction>() {
                @Override
                public void call(PerformAction performAction) {
                    feedPost.getContent().getActionsPerformed().setReblogged(true);
                }
            });
    }

    private void _doActionLike(final FeedPost feedPost) {
        feedientService.doActionTumblrLike(accessToken, feedPost.getProvider().getId(), "like", feedPost.getId(), feedPost.getTumblr().getReblogKey())
            .subscribe(new Action1<PerformAction>() {
                @Override
                public void call(PerformAction performAction) {
                    feedPost.getContent().getActionsPerformed().setLiked(true);
                }
            });
    }

    private void _doActionUnlike(final FeedPost feedPost) {
        feedientService.undoActionTumblrLike(accessToken, feedPost.getProvider().getId(), "unlike", feedPost.getId(), feedPost.getTumblr().getReblogKey())
            .subscribe(new Action1<PerformAction>() {
                @Override
                public void call(PerformAction performAction) {
                    feedPost.getContent().getActionsPerformed().setLiked(false);
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
        feedientService.addOAuth1Provider(accessToken, NAME, requestSecret, oAuthToken, oAuthVerifier)
            .subscribe(new Action1<List<UserProvider>>() {
                @Override
                public void call(List<UserProvider> userProviders) {
                    callback.onSuccess(userProviders);
                }
            });
    }

    @Override
    public void popup(final String accessToken, final IAddProviderCallback callback) {
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
                callback.success(getRequestToken);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public List<ProviderAction> getActions() {
        return actions;
    }
}