package com.feedient.core.api;

import com.feedient.core.model.User;
import com.feedient.core.models.json.Account;
import com.feedient.core.models.json.feed.FeedPostList;
import com.feedient.core.models.json.response.Logout;
import com.feedient.core.models.json.response.PerformAction;
import com.feedient.core.models.json.response.PostMessage;
import com.feedient.oauth.models.GetRequestToken;
import com.feedient.core.models.json.response.RemoveUserProvider;
import com.feedient.core.models.json.UserProvider;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.http.*;
import retrofit.mime.TypedFile;
import rx.Observable;

import java.util.List;

public interface FeedientService {
    @GET("/user")
    public abstract void getAccount(@Header("Bearer")String paramAccessToken, Callback<UserResponse> paramCallback);

    @FormUrlEncoded
    @POST("/user/authorize")
    public abstract void login(@Field("email")String paramEmail, @Field("password")String paramPassword, Callback<FeedientService.LoginResponse> paramCallback);

    @GET("/provider")
    Observable<List<UserProvider>> getProviders(@Header("Bearer")String accessToken);

    @FormUrlEncoded
    @POST("/providers/feed")
    Observable<FeedPostList> getFeeds(@Header("Bearer")String accessToken, @Field("providers")JSONArray providers);

    @FormUrlEncoded
    @POST("/providers/feed/new")
    Observable<FeedPostList> getNewerPosts(@Header("Bearer")String accessToken, @Field("objects")JSONArray objects);

    @FormUrlEncoded
    @POST("/providers/feed/old")
    Observable<FeedPostList> getOlderPosts(@Header("Bearer")String accessToken, @Field("objects")JSONArray objects);

    @DELETE("/provider/{id}")
    Observable<RemoveUserProvider> removeUserProvider(@Header("Bearer")String accessToken, @Path("id")String providerId);

    @FormUrlEncoded
    @POST("/provider/{name}/callback")
    Observable<List<UserProvider>> addOAuth2Provider(@Header("Bearer")String accessToken, @Path("name")String providerName, @Field("oauth_code")String oauthCode);

    @FormUrlEncoded
    @POST("/provider/{name}/callback")
    Observable<List<UserProvider>> addOAuth1Provider(@Header("Bearer")String accessToken, @Path("name")String providerName, @Field("oauth_secret")String oAuthSecret, @Field("oauth_token")String oAuthToken, @Field("oauth_verifier")String oAuthVerifier);

    @GET("/provider/{name}/request_token")
    void getRequestToken(@Header("Bearer")String accessToken, @Path("name")String providerName, Callback<GetRequestToken> cb);

    @GET("/logout")
    Observable<Logout> logout(@Header("Bearer")String accessToken);

    // ACTIONS
    // Facebook
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionFacebookLike(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("post_id") String id);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionFacebookLike(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("post_id") String id);

    // Twitter
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionTwitterFavorite(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("tweet_id") String tweetId);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionTwitterFavorite(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("tweet_id") String tweetId);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionTwitterRetweet(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("tweet_id") String tweetId);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionTwitterRetweet(@Header("Bearer") String accessToken, @Path("userProviderId") String userProviderId, @Path("actionMethod") String actionMethod, @Field("tweet_id") String tweetId);

    // Instagram
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionInstagramLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionInstagramLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id);

    // Tumblr
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionTumblrLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id, @Field("reblog_key")String reblogKey);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionTumblrLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id, @Field("reblog_key")String reblogKey);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionTumblrReblog(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id, @Field("reblog_key")String reblogKey);

    // YouTube
    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionYoutubeLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> undoActionYoutubeLike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id);

    @FormUrlEncoded
    @POST("/provider/{userProviderId}/action/{actionMethod}")
    Observable<PerformAction> doActionYoutubeDislike(@Header("Bearer")String accessToken, @Path("userProviderId")String userProviderId, @Path("actionMethod")String actionMethod, @Field("media_id")String id);

    // Send message
    @FormUrlEncoded
    @POST("/providers/message")
    void postMessage(@Header("Bearer")String accessToken, @Field("providers")String providerIds, @Field("message")String message, Callback<PostMessage[]> cb);

    @Multipart
    @POST("/providers/pictures")
    void postMessageWithPicture(@Header("Bearer")String accessToken, @Part("providers")String providerIds, @Part("message")String message, @Part("picture")TypedFile picture, Callback<PostMessage[]> cb);

    public class LoginResponse {
        private String uid;
        private String token;

        public String getUid() {
            return uid;
        }

        public String getToken() {
            return token;
        }
    }

    public class UserResponse extends User {
    }
}
