package com.feedient.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.feedient.android.R;
import com.feedient.android.adapters.ItemArrayAdapter;
import com.feedient.android.models.ViewAllFeeds;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.Observable;
import java.util.Observer;

public class ViewAllFeedsActivity extends Activity implements Observer, OnRefreshListener {
    private ItemArrayAdapter itemArrayAdapter;
    private ViewAllFeeds viewAllFeeds;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mListView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_feeds); // @todo: When loading, set a loading icon

        viewAllFeeds = new ViewAllFeeds(this);
        viewAllFeeds.addObserver(this);
        viewAllFeeds.loadFeeds();

        mListView = (ListView)findViewById(R.id.list);
        itemArrayAdapter = new ItemArrayAdapter(this, viewAllFeeds.getFeedPosts());
        mListView.setAdapter(itemArrayAdapter);

        mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Start auto update
        viewAllFeeds.initAutoUpdateTimer();
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Open settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemArrayAdapter.notifyDataSetChanged();
                mPullToRefreshLayout.setRefreshing(viewAllFeeds.isRefreshing());
            }
        });
    }

    public ItemArrayAdapter getItemArrayAdapter() {
        return itemArrayAdapter;
    }

    public void setItemArrayAdapter(ItemArrayAdapter itemArrayAdapter) {
        this.itemArrayAdapter = itemArrayAdapter;
    }

    @Override
    public void onRefreshStarted(View view) {
        viewAllFeeds.loadNewPosts();
    }
}
