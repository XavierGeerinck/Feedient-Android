package com.feedient.models;

import com.google.gson.annotations.SerializedName;

public class RestError {
    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String errorDetails;
}
