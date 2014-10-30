package com.feedient.core.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import com.feedient.core.R;
import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.api.FeedientService;
import com.feedient.core.data.AssetsPropertyReader;

import java.util.Properties;

public class MainActivity extends Activity {
    // Drawer variables
    private DrawerLayout mDrawerLayout;
    private ListView mNavDrawerList;
    private ListView mPanelsList;
    private ActionBarDrawerToggle mDrawerToggle;

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
        setContentView(R.layout.view_main); // @todo: When loading, set a loading icon

        mTitle = mDrawerTitle = getTitle();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Views
        mDrawerLayout           = (DrawerLayout)findViewById(R.id.drawer_layout);
        //mBtnCompose             = (FloatingActionButton)findViewById(R.id.btn_compose);

        // Services
        assetsPropertyReader = new AssetsPropertyReader(getApplicationContext());
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        configProperties = assetsPropertyReader.getProperties("config.properties");
        sharedPreferences = getApplicationContext().getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        feedientService = new FeedientRestAdapter(getApplicationContext()).getService();

        // Variables
        accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");

    }

    private void initDrawerMenuItems() {
//        String[] navMenuTitles = getApplicationContext().getResources().getStringArray(R.array.nav_drawer_items);
//        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
//
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], "{fa-plus}")); // Add provider
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], "{fa-sign-out}")); // Sign Out
//
//        mNavDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
//        mNavDrawerList.setAdapter(mNavDrawerListAdapter);
    }
}
