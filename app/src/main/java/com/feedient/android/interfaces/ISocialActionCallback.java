package com.feedient.android.interfaces;

import com.feedient.android.models.json.schema.FeedPost;

public interface ISocialActionCallback {
    public void handleOnClick(FeedPost feedPost);
}
