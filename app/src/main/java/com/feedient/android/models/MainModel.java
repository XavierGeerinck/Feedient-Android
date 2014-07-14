package com.feedient.android.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.feedient.android.R;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.interfaces.IAddProviderCallback;
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
import rx.functions.Action1;

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
        feedientService.getAccount(accessToken)
            .subscribe(new Action1<Account>() {
                @Override
                public void call(Account account) {
                    MainModel.this.account.setId(account.getId());
                    MainModel.this.account.setEmail(account.getEmail());
                    MainModel.this.account.setLanguage(account.getLanguage());
                    MainModel.this.account.setRole(account.getRole());

                    _triggerObservers();
                }
            });
    }

    /**
     * Loads the last X posts of the user
     */
    public void loadFeeds() {
        feedientService.getProviders(accessToken)
            .subscribe(new Action1<List<UserProvider>>() {
                @Override
                public void call(List<UserProvider> userProviders) {
                    JSONArray userProviderIds = new JSONArray();

                    for (UserProvider up : userProviders) {
                        userProviderIds.put(up.getId());
                        MainModel.this.userProviders.add(up);
                    }

                    _triggerObservers();

                    // Get all the feeds
                    feedientService.getFeeds(accessToken, userProviderIds)
                            .subscribe(new Action1<FeedPostList>() {
                                @Override
                                public void call(FeedPostList feedPostList) {
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
                            });
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

        feedientService.getNewerPosts(accessToken, newFeedPosts)
            .subscribe(new Action1<FeedPostList>() {
                @Override
                public void call(FeedPostList feedPostList) {
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
            });
    }

    public void loadOlderPosts() {
        isRefreshing = true;
        _triggerObservers();
//
//        JSONArray newFeedPosts = new JSONArray();
//
//        // Get the last posts for every provider, and add the until key
//        for (UserProvider up : userProviders) {
//            String userProviderId = up.getId();
//            String until = paginationKeys.get(up.getId());
//
//            try {
//                newFeedPosts.put(new NewFeedPost(userProviderId, since));
//            } catch (JSONException e) {
//                Log.e("Feedient", e.getMessage());
//            }
//        }

        Log.e("Feedient", "LOADING OLDER POSTS");
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
        feedientService.logout(this.accessToken)
            .subscribe(new Action1<Logout>() {
                @Override
                public void call(Logout logout) {
                    _removeAccessToken();
                    _triggerObservers();
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

    public void removeUserProvider(final UserProvider up) {
        feedientService.removeUserProvider(accessToken, up.getId())
            .subscribe(new Action1<RemoveUserProvider>() {
                @Override
                public void call(RemoveUserProvider removeUserProvider) {
                    userProviders.remove(up);
                    _triggerObservers();
                }
            });
    }

    public void addUserProvider(IProviderModel provider) {
        provider.popup(context, accessToken, new IAddProviderCallback() {
            @Override
            public void onSuccess(List<UserProvider> addedUserProviders) {
                for (UserProvider up : addedUserProviders) {
                    userProviders.add(up);
                }

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
