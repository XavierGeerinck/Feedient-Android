package com.feedient.android.interfaces;

import com.feedient.android.models.json.feed.FeedPostList;
import com.feedient.android.models.json.request.NewFeedPost;
import com.feedient.android.models.json.schema.FeedPost;
import com.feedient.android.models.json.feed.FeedResult;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.UserSession;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.http.*;

import java.util.List;

public interface FeedientService {
    @FormUrlEncoded
    @POST("/user/authorize")
    void authorizeUser(@Field("email") String email, @Field("password") String password, Callback<UserSession> cb);

    @GET("/provider")
    void getProviders(@Header("Bearer") String accessToken, Callback<List<UserProvider>> cb);

    @GET("/provider/{providerId}/feed")
    void getFeed(@Header("Bearer") String accessToken, @Path("providerId") String providerId, Callback<FeedResult> cb);

    @FormUrlEncoded
    @POST("/providers/feed")
    void getFeeds(@Header("Bearer") String accessToken, @Field("providers") List<String> providers, Callback<FeedPostList> cb);

    @FormUrlEncoded
    @POST("/providers/feed/new")
    void getNewerPosts(@Header("Bearer") String accessToken, @Field("objects") List<NewFeedPost> objects, Callback<FeedPostList> cb);
}