package com.feedient.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewPostsSchema {
    @SerializedName("")
    public List<NewPostsProvider> newPostsProviders;

    public NewPostsSchema() {
        newPostsProviders = new ArrayList<NewPostsProvider>();
    }

    public List<NewPostsProvider> getNewPostsProviders() {
        return newPostsProviders;
    }

    public void setNewPostsProviders(List<NewPostsProvider> newPostsProviders) {
        this.newPostsProviders = newPostsProviders;
    }
}
