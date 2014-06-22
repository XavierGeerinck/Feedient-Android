package com.feedient.interfaces;

import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.models.json.schema.FeedPost;

import java.util.List;

public interface IViewAllFeeds {
    public List<FeedPost> getFeedPosts();
    public void setFeedPosts(List<FeedPost> feedPosts);
    public int getNewNotifications();
    public void setNewNotifications(int newNotifications);
    public void triggerObservers();
}
