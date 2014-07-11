package com.feedient.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.adapters.DrawerItemAdapter;
import com.feedient.android.adapters.FeedListAdapter;
import com.feedient.android.adapters.GridItemAdapter;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.GridItem;
import com.feedient.android.models.MainModel;

import com.feedient.android.models.json.UserProvider;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer, OnRefreshListener {
    private FeedListAdapter mFeedListAdapter;
    private DrawerItemAdapter mDrawerItemAdapter;
    private MainModel mMainModel;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mFeedPostsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main); // @todo: When loading, set a loading icon

        // Init observers
        mMainModel = new MainModel(this);
        mMainModel.addObserver(this);
        mMainModel.loadUser();
        mMainModel.loadFeeds();

        // Init the views
        mFeedPostsList      = (ListView)findViewById(R.id.list);
        mDrawerLayout       = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList         = (ListView)findViewById(R.id.drawer_provider_list);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Feedient");
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle("Settings");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Fix margin top and bottom for list of cards
        mFeedPostsList.addFooterView(new View(this), null, false);
        mFeedPostsList.addHeaderView(new View(this), null, false);

        // Set the adapter for our feed
        mFeedListAdapter = new FeedListAdapter(this, mMainModel.getFeedPosts());
        mFeedPostsList.setAdapter(mFeedListAdapter);

        // Set the adapter for our drawer list
        mDrawerItemAdapter = new DrawerItemAdapter(this, mMainModel.getUserProviders(), mMainModel.getProviders());
        mDrawerList.setAdapter(mDrawerItemAdapter);

        mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Start auto update
        //mMainModel.initAutoUpdateTimer();
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync toggle state after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLoggedIn();

                mFeedListAdapter.notifyDataSetChanged();
                mDrawerItemAdapter.notifyDataSetChanged();
                mPullToRefreshLayout.setRefreshing(mMainModel.isRefreshing());
            }
        });
    }

    private void checkLoggedIn() {
        if (mMainModel.getAccessToken() == null || mMainModel.getAccessToken().equals("")) {
            openLoginActivity();
        }
    }

    public void onClickLogout(final View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_sign_out_title)
                .setMessage(R.string.dialog_sign_out_message)
                .setPositiveButton(R.string.choice_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    mMainModel.logout();
                    }
                })
                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close current intent
    }

    public void onClickRemoveUserProvider(final View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_remove_user_provider_title)
                .setMessage(R.string.dialog_remove_user_provider_message)
                .setPositiveButton(R.string.choice_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserProvider up = (UserProvider)v.getTag();
                        mMainModel.removeUserProvider(up);
                        mDrawerItemAdapter.remove(up);
                    }
                })
                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    public void onClickAddUserProvider(View v) {
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_grid, null);
        final List<GridItem> items = new ArrayList<GridItem>();

        for (IProviderModel provider : mMainModel.getProviders().values()) {
            items.add(new GridItem(provider.getName(), provider));
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);

        final AlertDialog pickProviderDialog = builder.create();
        pickProviderDialog.show();

        GridView gridView = (GridView)customView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridItemAdapter(this, items));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // call the OAuth popup
                mMainModel.addProvider(items.get(position).getProviderModel());

                // Close the other dialog
                pickProviderDialog.dismiss();
            }
        });
    }

    @Override
    public void onRefreshStarted(View view) {
        mMainModel.loadNewPosts();
    }
}
