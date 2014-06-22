package com.feedient.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.feedient.models.feed.FeedPost;
import com.feedient.models.feed.FeedResult;
import com.feedient.models.UserProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ViewAllFeedsActivity extends ListActivity {
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_all_feeds); // @todo: When loading, set a loading icon

        assetsPropertyReader = new AssetsPropertyReader(this);
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        sharedPreferences = getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        feedientService = new FeedientRestAdapter(this).getService();

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
                        Log.e("Feedient", "Changed the list adapter, it contains: " + feedPosts.size() + " posts!");
                        setListAdapter(new ItemArrayAdapter(ViewAllFeedsActivity.this, feedPosts));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }
}
