package com.feedient.core.models.json.schema;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Notification {
    @SerializedName("id")
    private String id;

    @SerializedName("created_time")
    private Date createdTime;

    @SerializedName("link")
    private String link;

    @SerializedName("read")
    private Integer read;

    @SerializedName("user_from")
    private User userFrom;

    @SerializedName("user_to")
    private User userTo;

    @SerializedName("content")
    private NotificationContent content;

    public Notification() {
        id = "";
        createdTime = new Date();
        link = "";
        read = 0;
        userFrom = new User();
        userTo = new User();
        content = new NotificationContent();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public NotificationContent getContent() {
        return content;
    }

    public void setContent(NotificationContent content) {
        this.content = content;
    }
}
