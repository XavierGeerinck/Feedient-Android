package com.feedient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.feedient.R;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.feedient.models.UserProvider;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

public class ViewAllFeedsActivity extends Activity {
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
        setContentView(R.layout.view_all_feeds);

        assetsPropertyReader = new AssetsPropertyReader(this);
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        sharedPreferences = getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        feedientService = new FeedientRestAdapter(this).getService();

        String accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "NO_ACCESS_TOKEN_FOUND");
        feedientService.getProviders(accessToken, new Callback<List<UserProvider>>() {
            @Override
            public void success(List<UserProvider> userProviders, Response response) {
                for (UserProvider up : userProviders) {
                    Toast.makeText(getApplicationContext(), "UserProvider: " + up.getProviderAccount().getName(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }
}
