package com.feedient.android.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.helpers.ProviderHelper;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.models.json.Account;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.feed.BulkPagination;
import com.feedient.android.models.json.feed.FeedPostList;
import com.feedient.android.models.json.request.NewFeedPost;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.android.models.json.schema.FeedPost;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.*;

public class MainModel extends Observable {
    private Context context;

    private final long timerInterval;
    private int newNotifications;
    private List<FeedPost> feedPosts;
    private Map<String, String> paginationKeys; // <userProviderId, since>
    private List<UserProvider> userProviders;
    private Account account;

    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private Properties configProperties;
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;

    private boolean isRefreshing;

    public MainModel(Context context) {
        this.context = context;

        feedPosts = new ArrayList<FeedPost>();
        paginationKeys = new HashMap<String, String>();
        userProviders = new ArrayList<UserProvider>();
        account = new Account();
        newNotifications = 0;

        assetsPropertyReader = new AssetsPropertyReader(context);
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        configProperties = assetsPropertyReader.getProperties("config.properties");
        sharedPreferences = context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        feedientService = new FeedientRestAdapter(context).getService();

        isRefreshing = false;

        timerInterval = Long.parseLong(configProperties.getProperty("auto_update_interval"));
    }

    public void loadUser() {
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.getAccount(accessToken, new Callback<Account>() {
            @Override
            public void success(Account account, Response response) {
                MainModel.this.account.setId(account.getId());
                MainModel.this.account.setEmail(account.getEmail());
                MainModel.this.account.setLanguage(account.getLanguage());
                MainModel.this.account.setRole(account.getRole());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
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
                List<String> providerIds = new ArrayList<String>();
                for (UserProvider up : userProviders) {
                    MainModel.this.userProviders.add(up);
                    providerIds.add(up.getId());
                }

                _triggerObservers();

                // Get all the feeds
                feedientService.getFeeds(accessToken, providerIds, new Callback<FeedPostList>() {
                    @Override
                    public void success(FeedPostList feedPostList, Response response) {
                        // Set the posts
                        for (FeedPost fp : feedPostList.getFeedPosts()) {
                            MainModel.this.feedPosts.add(fp);
                        }

                        // Set the paginations
                        for (BulkPagination bp : feedPostList.getPaginations()) {
                            paginationKeys.put(bp.getProviderId(), bp.getSince());
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
        isRefreshing = true;
        _triggerObservers();

        List<NewFeedPost> newFeedPosts = new ArrayList<NewFeedPost>();

        // Get the last posts for every provider, and add the since key
        for (UserProvider up : userProviders) {
            String userProviderId = up.getId();
            String since = paginationKeys.get(up.getId());

            Log.e("Feedient", "Since: " + since);
            try {
                newFeedPosts.add(new NewFeedPost(userProviderId, since));
            } catch (JSONException e) {
                Log.e("Feedient", e.getMessage());
            }
        }

        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.getNewerPosts(accessToken, newFeedPosts, new Callback<FeedPostList>() {
            @Override
            public void success(FeedPostList feedPostList, Response response) {
                Log.e("Feedient", "New posts: " + feedPostList.getFeedPosts().size());
                // Add posts to the beginning (Start at the end of the array for ordering)
                for (int i = feedPostList.getFeedPosts().size() - 1; i >= 0; i--) {
                    FeedPost fp = feedPostList.getFeedPosts().get(i);
                    MainModel.this.feedPosts.add(0, fp);
                }

                // Set the paginations
                for (BulkPagination bp : feedPostList.getPaginations()) {
                    paginationKeys.put(bp.getProviderId(), bp.getSince());
                }

                // We got list items added, trigger observers
                isRefreshing = false;
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

    public void initAutoUpdateTimer() {
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNewPosts();
                h.postDelayed(this, timerInterval);
            }
        }, timerInterval);
    }

    private void _triggerObservers() {
        setChanged();
        notifyObservers();
    }

    public FeedPost getLastPostProvider(String providerId) {
        for (FeedPost fp : feedPosts) {
            if (fp.getProvider().getId().equals(providerId)) {
                return fp;
            }
        }

        return null;
    }

    public void removeUserProvider(UserProvider up) {
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.removeUserProvider(accessToken, up.getId(), new Callback<RemoveUserProvider>() {
            @Override
            public void success(RemoveUserProvider rup, Response response) {
                Log.e("Feedient", "Remove User Provider: " + rup.isSuccess());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
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

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void addProvider(String providerName, JSONObject jo) {
        final String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        try {
            ProviderHelper.providerNameToClass(providerName).addProvider(accessToken, feedientService, jo);
        } catch (JSONException e) {
            Log.e("Feedient", e.getMessage());
        }
    }
}
