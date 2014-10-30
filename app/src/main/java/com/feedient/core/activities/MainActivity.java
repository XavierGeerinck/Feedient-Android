package com.feedient.core.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.feedient.core.R;
import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.api.FeedientService;
import com.feedient.core.data.AssetsPropertyReader;

import java.util.Properties;

public class MainActivity extends Activity {
    // Navigation Drawer
    private String[] mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // App title
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private Properties configProperties;
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;
    private String accessToken;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // @todo: When loading, set a loading icon

        // Load main variables
        init();

        // Set drawer
        mDrawerItems = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItems));

        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void init() {
        // Services
        assetsPropertyReader = new AssetsPropertyReader(getApplicationContext());
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        configProperties = assetsPropertyReader.getProperties("config.properties");
        sharedPreferences = getApplicationContext().getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        feedientService = new FeedientRestAdapter(getApplicationContext()).getService();

        // Variables
        accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");
    }
}
