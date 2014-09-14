package com.feedient.panel.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feedient.core.activities.LoginActivity;
import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.adapters.NavDrawerListAdapter;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.interfaces.FeedientService;
import com.feedient.R;
import com.feedient.panel.models.PanelModel;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import rx.functions.Action1;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class PanelActivity extends Activity implements Observer, OnRefreshListener {
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;

    private PanelModel panelModel;

    private ListView mPanelList;
    private ListView mMenuList;

    // App title
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    // Feed variables
    private PullToRefreshLayout mPullToRefreshLayout;

    // Drawer variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_view_main); // @todo: When loading, set a loading icon

        // set title
        this.mTitle = this.mDrawerTitle = getTitle();

        // Init observers
        this.panelModel = new PanelModel(this);
        this.panelModel.addObserver(this);
        this.panelModel.loadUser();

        // Load components from view
        this.mPanelList = (ListView) findViewById(R.id.drawer_panel_list);
        this.mMenuList = (ListView) findViewById(R.id.drawer_menu_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // Load service
        this.assetsPropertyReader = new AssetsPropertyReader(this);
        this.properties = this.assetsPropertyReader.getProperties("shared_preferences.properties");
        this.sharedPreferences = getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        this.feedientService = new FeedientRestAdapter(this).getService();
        String accessToken = this.sharedPreferences.getString(this.properties.getProperty("prefs.key.token"), "");
        this.feedientService.getAccount(accessToken).subscribe(new Action1<FeedientService.AccountResponse>() {
            @Override
            public void call(FeedientService.AccountResponse accountResponse) {
                Log.e("feedient", accountResponse.email);
            }
        });

        // Set the adapters for panel and menu items
        NavDrawerListAdapter listAdapter = new NavDrawerListAdapter(getApplicationContext(), panelModel.getNavDrawerPanelItems());
        mPanelList.setAdapter(listAdapter);

        listAdapter = new NavDrawerListAdapter(getApplicationContext(), panelModel.getNavDrawerMenuItems());
        mMenuList.setAdapter(listAdapter);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);

        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout,
                R.drawable.ic_navigation_drawer, // Toggle icon
                R.string.drawer_open,            // Description onOpen
                R.string.drawer_close            // Description onClose
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        this.mPanelList.setOnItemClickListener(new DrawerItemClickListener());
        this.mMenuList.setOnItemClickListener(new DrawerItemClickListener());

        this.mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(this.mPullToRefreshLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the settings to the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                // Open settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync toggle state after onRestoreInstanceState has occurred
        this.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLoggedIn();

//                mFeedListAdapter.notifyDataSetChanged();
//                mNavDrawerListAdapter.notifyDataSetChanged();
//                mNavDrawerProvidersListAdapter.notifyDataSetChanged();
//                mPullToRefreshLayout.setRefreshing(panelModel.isRefreshing());
            }
        });
    }

    @Override
    public void onRefreshStarted(View view) {
        //do nothing
    }

    private void checkLoggedIn() {
        if (panelModel.getAccessToken() == null || panelModel.getAccessToken().equals("")) {
            this.openLoginActivity();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(PanelActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close current intent
    }

    /**
     * DrawerItemListener -  OnItemClick in the drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            executeAction(position);
        }
    }

    private void executeAction(int position) {
        View view = getWindow().getDecorView().getRootView();

        switch (position) {
            // Logout
            case 1:
                actionLogout(view);
                break;
        }
    }

    public void actionLogout(final View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_sign_out_title)
                .setMessage(R.string.dialog_sign_out_message)
                .setPositiveButton(R.string.choice_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        panelModel.signout();
                    }
                })
                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }
}
