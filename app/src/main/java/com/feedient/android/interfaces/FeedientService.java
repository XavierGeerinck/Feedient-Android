package com.feedient.android.interfaces;

import com.feedient.android.models.json.Account;
import com.feedient.android.models.json.feed.FeedPostList;
import com.feedient.android.models.json.response.AddProvider;
import com.feedient.android.models.json.response.Logout;
import com.feedient.android.models.json.response.PerformAction;
import com.feedient.oauth.models.GetRequestToken;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.android.models.json.feed.FeedResult;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.UserSession;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.http.*;

import java.util.List;

public interface FeedientService {
    @GET("/user")
    void getAccount(@Header("Bearer")String accessToken, Callback<Account> cb);

    @FormUrlEncoded
    @POST("/user/authorize")
    void authorizeUser(@Field("email")String email, @Field("password")String password, Callback<UserSession> cb);

    @GET("/provider")
    void getProviders(@Header("Bearer")String accessToken, Callback<List<UserProvider>> cb);

    @GET("/provider/{providerId}/feed")
    void getFeed(@Header("Bearer")String accessToken, @Path("providerId")String providerId, Callback<FeedResult> cb);

    @FormUrlEncoded
    @POST("/providers/feed")
    void getFeeds(@Header("Bearer")String accessToken, @Field("providers")JSONArray providers, Callback<FeedPostList> cb);

    @FormUrlEncoded
    @POST("/providers/feed/new")
    void getNewerPosts(@Header("Bearer")String accessToken, @Field("objects")JSONArray objects, Callback<FeedPostList> cb);

    @DELETE("/provider/{id}")
    void removeUserProvider(@Header("Bearer")String accessToken, @Path("id")String providerId, Callback<RemoveUserProvider> cb);

    @FormUrlEncoded
    @POST("/provider/{name}/callback")
    void addOAuth2Provider(@Header("Bearer")String accessToken, @Path("name")String providerName, @Field("oauth_code")String oauthCode, Callback<List<UserProvider>> cb);

    @FormUrlEncoded
    @POST("/provider/{name}/callback")
    void addOAuth1Provider(@Header("Bearer")String accessToken, @Path("name")String providerName, @Field("oauth_secret")String oAuthSecret, @Field("oauth_token")String oAuthToken, @Field("oauth_verifier")String oAuthVerifier, Callback<List<UserProvider>> cb);

    @GET("/provider/{name}/request_token")
    void getRequestToken(@Header("Bearer")String accessToken, @Path("name")String providerName, Callback<GetRequestToken> cb);

    @GET("/logout")
    void logout(@Header("Bearer")String accessToken, Callback<Logout> cb);

    // ACTIONS
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void doActionLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("post_id")String id, Callback<PerformAction> cb);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void undoActionLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("post_id")String id, Callback<PerformAction> cb);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void doActionFavorite(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("tweet_id")String tweetId, Callback<PerformAction> cb);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void undoActionFavorite(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("tweet_id")String tweetId, Callback<PerformAction> cb);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void doActionRetweet(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("tweet_id")String tweetId, Callback<PerformAction> cb);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    void undoActionRetweet(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("tweet_id")String tweetId, Callback<PerformAction> cb);

}
