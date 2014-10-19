package com.feedient.core.models.providers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.IconButton;

import com.feedient.core.R;
import com.feedient.core.api.FeedientService;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.interfaces.ISocialActionCallback;
import com.feedient.core.models.json.UserProvider;
import com.feedient.core.models.json.response.PerformAction;
import com.feedient.core.models.json.schema.FeedPost;
import com.feedient.oauth.OAuthDialog;
import com.feedient.core.interfaces.IAddProviderCallback;
import com.feedient.oauth.interfaces.IOAuth2Provider;
import com.feedient.oauth.webview.WebViewCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

public class YouTube implements IProviderModel, IOAuth2Provider {
    public static final String NAME = "youtube";
    public static final String TEXT_COLOR = "#b31217";
    public static final String ICON = "fa-youtube-play";
    public static final String APP_ID = "1053845138024-8idrb4t2qjpd2rtq41967cd9vg6kr360.apps.googleusercontent.com";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/youtube";
    public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth?client_id=" + APP_ID + "&response_type=code&scope=https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtube.readonly https://www.googleapis.com/auth/youtube&access_type=offline&approval_prompt=force&redirect_uri=" + OAUTH_CALLBACK_URL;

    private Context context;
    private FeedientService feedientService;
    private String accessToken;
    private List<ProviderAction> actions;

    public YouTube(Context context, FeedientService feedientService, String accessToken) {
        this.context = context;
        this.feedientService = feedientService;
        this.accessToken = accessToken;
        this.actions = new ArrayList<ProviderAction>();

        _initActions();
    }

    private void _initActions() {
        actions.add(new ProviderAction("like", "liked", "{fa-thumbs-up}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(IconButton button, FeedPost feedPost) {
                if (!feedPost.getContent().getActionsPerformed().isLiked()) {
                    _doActionLike(feedPost);
                    button.setTextColor(Color.parseColor(TEXT_COLOR));
                } else {
                    _doActionUnlike(feedPost);
                    button.setTextColor(context.getResources().getColor(R.color.feed_item_action_social_icon));
                }
            }
        }));

        actions.add(new ProviderAction("dislike", "disliked", "{fa-thumbs-down}", new ISocialActionCallback() {
            @Override
            public void handleOnClick(IconButton button, FeedPost feedPost) {
                _doActionDislike(feedPost);
                button.setTextColor(Color.parseColor(TEXT_COLOR));
            }
        }));
    }

    private void _doActionDislike(final FeedPost feedPost) {
        feedientService.doActionYoutubeDislike(accessToken, feedPost.getProvider().getId(), "dislike", feedPost.getId())
            .subscribe(new Action1<PerformAction>() {
                @Override
                public void call(PerformAction performAction) {
                    feedPost.getContent().getActionsPerformed().setDisliked(true);
                }
            });
    }

    private void _doActionLike(final FeedPost feedPost) {
        feedientService.doActionYoutubeLike(accessToken, feedPost.getProvider().getId(), "like", feedPost.getId())
            .subscribe(new Action1<PerformAction>() {
                @Override
                public void call(PerformAction performAction) {
                    feedPost.getContent().getActionsPerformed().setLiked(true);
                }
            });
    }

    private void _doActionUnlike(final FeedPost feedPost) {
        feedientService.undoActionYoutubeLike(accessToken, feedPost.getProvider().getId(), "unlike", feedPost.getId())
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

    public void addProvider(String accessToken, FeedientService feedientService, String oAuthCode, final IAddProviderCallback callback) {
        feedientService.addOAuth2Provider(accessToken, NAME, oAuthCode)
            .subscribe(new Action1<List<UserProvider>>() {
                @Override
                public void call(List<UserProvider> userProviders) {
                    callback.onSuccess(userProviders);
                }
            });
    }

    @Override
    public void popup(final String accessToken, final IAddProviderCallback callback) {
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

    public List<ProviderAction> getActions() {
        return actions;
    }
}