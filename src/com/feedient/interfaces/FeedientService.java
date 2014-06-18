package com.feedient.interfaces;

import com.feedient.models.UserProvider;
import com.feedient.models.UserSession;
import retrofit.Callback;
import retrofit.http.*;

import java.util.List;

public interface FeedientService {
    @FormUrlEncoded
    @POST("/user/authorize")
    void authorizeUser(@Field("email") String email, @Field("password") String password, Callback<UserSession> cb);

    @GET("/provider")
    void getProviders(@Header("Bearer") String accessToken, Callback<List<UserProvider>> cb);
}
