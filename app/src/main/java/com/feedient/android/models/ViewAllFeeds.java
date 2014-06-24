package com.feedient.android.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.schema.FeedPost;
import org.json.JSONArray;
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

    /**
     * Loads the last X posts of the user
     */
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
                        Log.e("Feedient", retrofitError.getMessage());
                    }
                });
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    /**
     * Loads the posts of the user since date X
     * @param sinceDate
     */
    public void loadFeeds(Date sinceDate) {

    }

    /**
     * Loads the posts that are older the date X
     * @param lastDate
     */
    public void loadOlderFeeds(Date lastDate) {

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
