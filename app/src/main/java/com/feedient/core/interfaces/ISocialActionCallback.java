package com.feedient.core.interfaces;

import com.feedient.core.models.json.schema.FeedPost;

public interface ISocialActionCallback {
    public void handleOnClick(FeedPost feedPost);
}
