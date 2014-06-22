package com.feedient.interfaces;

import com.feedient.models.json.schema.FeedPost;
import com.feedient.models.json.feed.FeedResult;
import com.feedient.models.json.UserProvider;
import com.feedient.models.json.UserSession;
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
    void getFeeds(@Header("Bearer") String accessToken, @Field("providers") String providers, Callback<List<FeedPost>> cb);
}
