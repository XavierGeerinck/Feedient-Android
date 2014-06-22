package com.feedient.models.json.socket;

import com.feedient.models.json.schema.FeedPost;
import com.feedient.models.json.schema.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SocketContent {
    @SerializedName("notifications")
    private List<Notification> notifications;

    @SerializedName("posts")
    private List<FeedPost> posts;

    public SocketContent() {
        notifications = new ArrayList<Notification>();
        posts = new ArrayList<FeedPost>();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<FeedPost> getPosts() {
        return posts;
    }

    public void setPosts(List<FeedPost> posts) {
        this.posts = posts;
    }
}
