package com.feedient.tasks;

import com.feedient.interfaces.IViewAllFeeds;


public class SocketTask implements Runnable {
    private String websocketUrl;
    private String accessToken;
    private IViewAllFeeds currentActivity;

    public SocketTask(String websocketUrl, String accessToken, IViewAllFeeds currentActivity) {
        this.websocketUrl = websocketUrl;
        this.accessToken = accessToken;
        this.currentActivity = currentActivity;
    }

    @Override
    public void run() {

    }


}
