package com.feedient.android.models.providers;

import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.response.RemoveUserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Facebook implements IProviderModel {
    public static final String NAME = "facebook";
    public static final String TEXT_COLOR = "#3b5998";
    public static final String ICON = "fa-facebook-square";
    public static final String APP_ID = "454088611354529";
    public static final String OAUTH_CALLBACK_URL = "http://test.feedient.com/app/callback/facebook";
    public static final String OAUTH_URL = "https://facebook.com/v2.0/dialog/oauth?client_id=" + APP_ID + "&display=popup&scope=read_stream,manage_notifications,publish_actions,publish_stream,user_photos,friends_photos,friends_likes,friends_videos,friends_status,friends_relationship_details,user_photos&redirect_uri=" + OAUTH_CALLBACK_URL;
    public static final String[] OAUTH_FRAGMENTS = { "oauth_code" };

    public Facebook() {

    }

    public String getTextColor() {
        return TEXT_COLOR;
    }

    public String getIcon() {
        return ICON;
    }

    @Override
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
}
