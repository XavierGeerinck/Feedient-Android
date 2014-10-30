package com.feedient.core.api;

import com.feedient.core.model.User;

import retrofit.Callback;
import retrofit.http.*;

public interface FeedientService {
    @GET("/user")
    public abstract void getAccount(@Header("Bearer")String paramAccessToken, Callback<UserResponse> paramCallback);

    @FormUrlEncoded
    @POST("/user/authorize")
    public abstract void login(@Field("email")String paramEmail, @Field("password")String paramPassword, Callback<FeedientService.LoginResponse> paramCallback);

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
