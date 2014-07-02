package com.feedient.android.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.feedient.android.R;
import com.feedient.android.adapters.DrawerItemAdapter;
import com.feedient.android.adapters.ItemArrayAdapter;
import com.feedient.android.models.ProviderDrawer;
import com.feedient.android.models.ViewAllFeeds;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.Observable;
import java.util.Observer;

public class ViewAllFeedsActivity extends Activity implements Observer, OnRefreshListener {
    private ItemArrayAdapter mItemArrayAdapter;
    private DrawerItemAdapter mDrawerItemAdapter;
    private ViewAllFeeds mViewAllFeedsModel;
    private ProviderDrawer mProviderDrawer;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mListView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_feeds); // @todo: When loading, set a loading icon

        // Init observers
        mViewAllFeedsModel = new ViewAllFeeds(this);
        mViewAllFeedsModel.addObserver(this);
        mViewAllFeedsModel.loadFeeds();

        mProviderDrawer = new ProviderDrawer(this);
        mProviderDrawer.addObserver(this);

        // Init the views
        mListView       = (ListView)findViewById(R.id.list);
        mDrawerLayout   = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList     = (ListView)findViewById(R.id.drawer_provider_list);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Feedient");
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle("Providers");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Set the adapter for our feed
        mItemArrayAdapter = new ItemArrayAdapter(this, mViewAllFeedsModel.getFeedPosts());
        mListView.setAdapter(mItemArrayAdapter);

        // Set the adapter for our drawer list
        mDrawerItemAdapter = new DrawerItemAdapter(this, mViewAllFeedsModel.getUserProviders());
        mDrawerList.setAdapter(mDrawerItemAdapter);

        mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Start auto update
        //mViewAllFeedsModel.initAutoUpdateTimer();
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
                mItemArrayAdapter.notifyDataSetChanged();
                mDrawerItemAdapter.notifyDataSetChanged();
                mPullToRefreshLayout.setRefreshing(mViewAllFeedsModel.isRefreshing());
            }
        });
    }

    public ItemArrayAdapter getmItemArrayAdapter() {
        return mItemArrayAdapter;
    }

    public void setmItemArrayAdapter(ItemArrayAdapter mItemArrayAdapter) {
        this.mItemArrayAdapter = mItemArrayAdapter;
    }

    @Override
    public void onRefreshStarted(View view) {
        mViewAllFeedsModel.loadNewPosts();
    }
}
