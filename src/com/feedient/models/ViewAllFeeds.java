package com.feedient.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.feedient.interfaces.IViewAllFeeds;
import com.feedient.models.json.UserProvider;
import com.feedient.models.json.schema.FeedPost;
import com.feedient.tasks.SocketTask;
import org.json.JSONArray;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

public class ViewAllFeeds extends Observable implements IViewAllFeeds {
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
                        triggerObservers();

                        Properties configProperties = assetsPropertyReader.getProperties("config.properties");
                        final String websocketUrl = configProperties.getProperty("api_server.websocket_url");

                        // Start socket listener on UI thread, this because we stream posts to it
                        new SocketTask(websocketUrl, accessToken, ViewAllFeeds.this).run();
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

    public void triggerObservers() {
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
