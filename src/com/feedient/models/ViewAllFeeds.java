package com.feedient.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.adapters.retrofit.ISODateAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.feedient.interfaces.IViewAllFeeds;
import com.feedient.models.json.UserProvider;
import com.feedient.models.json.schema.FeedPost;
import com.feedient.models.json.socket.SocketResponse;
import com.feedient.tasks.SocketTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.*;

public class ViewAllFeeds extends Observable {
    private Context context;

    private int newNotifications;
    private List<FeedPost> feedPosts;

    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;

    public ViewAllFeeds(Context context) {
        this.context = context;

        feedPosts = new ArrayList<FeedPost>();
        newNotifications = 0;

        assetsPropertyReader = new AssetsPropertyReader(context);
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        sharedPreferences = context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        feedientService = new FeedientRestAdapter(context).getService();
    }

    public void loadFeeds() {
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.getProviders(accessToken, new Callback<List<UserProvider>>() {
            @Override
            public void success(List<UserProvider> userProviders, Response response) {
                // Get the providerIds
                JSONArray providerIds = new JSONArray();
                for (UserProvider up : userProviders) {
                    providerIds.put(up.getId());
                }

                // Get all the feeds
                feedientService.getFeeds(accessToken, providerIds.toString(), new Callback<List<FeedPost>>() {
                    @Override
                    public void success(List<FeedPost> feedPosts, Response response) {
                        for (FeedPost fp : feedPosts) {
                            ViewAllFeeds.this.feedPosts.add(fp);
                        }

                        // We got list items added, trigger observers
                        _triggerObservers();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public void loadSocket() {
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        Properties configProperties = assetsPropertyReader.getProperties("config.properties");
        final String websocketUrl = configProperties.getProperty("api_server.websocket_url");

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
                    feedPosts.add(0, fp);
                    _triggerObservers();
                }
            }
            // If it is a notification, show a toast
            else if (socketResponse.getType().equals("notification")) {
                newNotifications += socketResponse.getContent().getNotifications().size();
                _triggerObservers();
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

    private void _triggerObservers() {
        setChanged();
        notifyObservers();
    }

    public int getNewNotifications() {
        return newNotifications;
    }

    public void setNewNotifications(int newNotifications) {
        this.newNotifications = newNotifications;
    }

    public List<FeedPost> getFeedPosts() {
        return feedPosts;
    }

    public void setFeedPosts(List<FeedPost> feedPosts) {
        this.feedPosts = feedPosts;
    }
}
