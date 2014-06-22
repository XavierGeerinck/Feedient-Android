package com.feedient.tasks;

import android.util.Log;
import com.feedient.adapters.retrofit.ISODateAdapter;
import com.feedient.interfaces.IViewAllFeeds;
import com.feedient.models.json.schema.FeedPost;
import com.feedient.models.json.socket.SocketResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

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
        AsyncHttpClient.getDefaultInstance().websocket(websocketUrl, null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception e, WebSocket webSocket) {
                if (e != null) {
                    Log.e("Feedient", e.getMessage());
                    return;
                }

                final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String s) {
                        Log.e("Feedient", s);
                        SocketResponse socketResponse = gson.fromJson(s, SocketResponse.class);
                        _handleSocketResponse(socketResponse);
                    }
                });

                // Authenticate
                authenticateSocket(webSocket, accessToken);
            }
        });
    }

    private void _handleSocketResponse(SocketResponse socketResponse) {
        if (socketResponse != null && socketResponse.getType() != null) {
            // If it is a post, add it to the list
            if (socketResponse.getType().equals("post")) {
                for (FeedPost fp : socketResponse.getContent().getPosts()) {
                    currentActivity.getFeedPosts().add(0, fp);
                    currentActivity.triggerObservers();
                }
            }
            // If it is a notification, show a toast
            else if (socketResponse.getType().equals("notification")) {
                currentActivity.setNewNotifications(currentActivity.getNewNotifications() + socketResponse.getContent().getNotifications().size());
                currentActivity.triggerObservers();
            }
        }
    }

    public void authenticateSocket(WebSocket webSocket, String accessToken) {
        // Authenticate
        JSONObject jo = new JSONObject();
        JSONObject message = new JSONObject();

        try {
            jo.put("type", "authenticate");
            message.put("token", accessToken);
            jo.put("message", message);
        } catch (JSONException e1) {
            Log.e("Feedient", e1.getMessage());
        }

        Log.e("Feedient", "Sending: " + jo.toString());

        webSocket.send(jo.toString());
    }
}
