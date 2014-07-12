package com.feedient.android.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.feedient.android.R;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.oauth.interfaces.IAddProviderCallback;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.Account;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.feed.BulkPagination;
import com.feedient.android.models.json.feed.FeedPostList;
import com.feedient.android.models.json.request.NewFeedPost;
import com.feedient.android.models.json.response.Logout;
import com.feedient.android.models.json.response.RemoveUserProvider;
import com.feedient.android.models.json.schema.FeedPost;
import com.feedient.android.models.providers.Facebook;
import com.feedient.android.models.providers.Instagram;
import com.feedient.android.models.providers.Tumblr;
import com.feedient.android.models.providers.Twitter;
import com.feedient.android.models.providers.YouTube;

import org.json.JSONArray;
import org.json.JSONException;

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
    private HashMap<String, IProviderModel> providers;
    private Account account;

    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private Properties configProperties;
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;
    private String accessToken;

    private List<NavDrawerItem> navDrawerItems;

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

        accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");
        isRefreshing = false;

        timerInterval = Long.parseLong(configProperties.getProperty("auto_update_interval"));

        initProviders();
        initMenuItems();
    }

    private void initMenuItems() {
        String[] navMenuTitles = context.getResources().getStringArray(R.array.nav_drawer_items);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], "{fa-plus}")); // Add provider
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], "{fa-sign-out}")); // Sign Out
    }

    private void initProviders() {
        providers = new HashMap<String, IProviderModel>();
        providers.put("facebook", new Facebook(context, getFeedientService(), accessToken));
        providers.put("twitter", new Twitter(context, getFeedientService(), accessToken));
        providers.put("instagram", new Instagram(context, getFeedientService(), accessToken));
        providers.put("youtube", new YouTube(context, getFeedientService(), accessToken));
        providers.put("tumblr", new Tumblr(context, getFeedientService(), accessToken));
    }

    public void loadUser() {
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
                accessToken = "";
                _triggerObservers();
            }
        });
    }

    /**
     * Loads the last X posts of the user
     */
    public void loadFeeds() {
        feedientService.getProviders(accessToken, new Callback<List<UserProvider>>() {
            @Override
            public void success(List<UserProvider> userProviders, Response response) {
                JSONArray userProviderIds = new JSONArray();

                for (UserProvider up : userProviders) {
                    userProviderIds.put(up.getId());
                    MainModel.this.userProviders.add(up);
                }

                _triggerObservers();

                // Get all the feeds
                feedientService.getFeeds(accessToken, userProviderIds, new Callback<FeedPostList>() {
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

        JSONArray newFeedPosts = new JSONArray();

        // Get the last posts for every provider, and add the since key
        for (UserProvider up : userProviders) {
            String userProviderId = up.getId();
            String since = paginationKeys.get(up.getId());

            try {
                newFeedPosts.put(new NewFeedPost(userProviderId, since));
            } catch (JSONException e) {
                Log.e("Feedient", e.getMessage());
            }
        }

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

    public void logout() {
        feedientService.logout(this.accessToken, new Callback<Logout>() {
            @Override
            public void success(Logout logout, Response response) {
                _removeAccessToken();
                _triggerObservers();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                _removeAccessToken();
                _triggerObservers();
                Log.e("Feedient", "Could not remove accessToken from server");
            }
        });
    }

    private void _removeAccessToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(properties.getProperty("prefs.key.token"));
        editor.apply();
        accessToken = "";
    }

    private void _triggerObservers() {
        setChanged();
        notifyObservers();
    }

    public void removeUserProvider(UserProvider up) {
        feedientService.removeUserProvider(accessToken, up.getId(), new Callback<RemoveUserProvider>() {
            @Override
            public void success(RemoveUserProvider rup, Response response) {
                _triggerObservers();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public List<FeedPost> getFeedPosts() {
        return feedPosts;
    }

    public List<UserProvider> getUserProviders() {
        return userProviders;
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

    public void addProvider(IProviderModel provider) {
        provider.popup(context, accessToken, new IAddProviderCallback() {
            @Override
            public void onSuccess(UserProvider provider) {
                userProviders.add(provider);
                _triggerObservers();
            }
        });
    }

    public FeedientService getFeedientService() {
        return feedientService;
    }

    public HashMap<String, IProviderModel> getProviders() {
        return providers;
    }

    public String getAccessToken() {
        return sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");
    }

    public List<NavDrawerItem> getNavDrawerItems() {
        return navDrawerItems;
    }
}
