package com.feedient.android.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.schema.FeedPost;
import com.google.gson.Gson;
import org.json.JSONArray;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.*;

public class ViewAllFeeds extends Observable {
    private Context context;

    private int newNotifications;
    private List<FeedPost> feedPosts;
    private List<UserProvider> userProviders;

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

                ViewAllFeeds.this.userProviders = userProviders;

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
     * Loads the new posts of the user
     */
    public void loadNewPosts() {
        NewPostsSchema newPostsSchema = new NewPostsSchema();

        // Get the last posts for every provider, and add the since key
        for (UserProvider up : userProviders) {
            // Set Provider Id
            NewPostsProvider npp = new NewPostsProvider();
            npp.setProviderId(up.getId());

            // Set Since
            FeedPost fp = feedPosts.get(0);
            npp.setSince(fp.getPagination().getSince());

            newPostsSchema.getNewPostsProviders().add(npp);
        }

        Log.e("Feedient", new JSONArray(newPostsSchema.getNewPostsProviders()).toString());
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.getNewerPosts(accessToken, new JSONArray(newPostsSchema.getNewPostsProviders()).toString(), new Callback<List<FeedPost>>() {
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

    /**
     * Loads the posts that are older the date X
     * @param lastDate
     */
    public void loadOlderPosts(Date lastDate) {

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

    public List<UserProvider> getUserProviders() {
        return userProviders;
    }

    public void setUserProviders(List<UserProvider> userProviders) {
        this.userProviders = userProviders;
    }
}
