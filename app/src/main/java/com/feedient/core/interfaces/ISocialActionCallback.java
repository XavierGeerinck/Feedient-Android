package com.feedient.core.interfaces;

import android.widget.IconButton;

import com.feedient.core.models.json.schema.FeedPost;

public interface ISocialActionCallback {
    public void handleOnClick(IconButton button, FeedPost feedPost);
}
