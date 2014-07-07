package com.feedient.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.feedient.android.R;
import com.feedient.android.adapters.DrawerItemAdapter;
import com.feedient.android.adapters.GridItemAdapter;
import com.feedient.android.adapters.ItemArrayAdapter;
import com.feedient.android.models.GridItem;
import com.feedient.android.models.MainModel;

import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.providers.Facebook;
import com.feedient.android.models.providers.Instagram;
import com.feedient.android.models.providers.Tumblr;
import com.feedient.android.models.providers.Twitter;
import com.feedient.android.models.providers.YouTube;
import com.feedient.oauth.OAuthDialog;
import com.feedient.oauth.webview.WebViewCallback;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer, OnRefreshListener {
    private ItemArrayAdapter mItemArrayAdapter;
    private DrawerItemAdapter mDrawerItemAdapter;
    private MainModel mMainModel;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mListView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mTxtDrawerUserRole;
    private TextView mTxtDrawerUserEmail;

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
        mListView           = (ListView)findViewById(R.id.list);
        mDrawerLayout       = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList         = (ListView)findViewById(R.id.drawer_provider_list);
        mTxtDrawerUserEmail = (TextView)findViewById(R.id.txt_user_email);
        mTxtDrawerUserRole  = (TextView)findViewById(R.id.txt_user_role);

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

        // Set the adapter for our feed
        mItemArrayAdapter = new ItemArrayAdapter(this, mMainModel.getFeedPosts());
        mListView.setAdapter(mItemArrayAdapter);

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
                mItemArrayAdapter.notifyDataSetChanged();
                mDrawerItemAdapter.notifyDataSetChanged();
                mPullToRefreshLayout.setRefreshing(mMainModel.isRefreshing());
                mTxtDrawerUserEmail.setText(mMainModel.getAccount().getEmail());
                mTxtDrawerUserRole.setText(mMainModel.getAccount().getRole());

            }
        });
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
        // Show popup to pick provider
        // Second dialog
        // Close the dialog for picking the provider

        String accessToken = mMainModel.getAccessToken();
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_grid, null);
        final List<GridItem> items = new ArrayList<GridItem>();
        items.add(new GridItem("Facebook", new Facebook(this, mMainModel.getFeedientService(), accessToken)));
        items.add(new GridItem("Twitter", new Twitter(this, mMainModel.getFeedientService(), accessToken)));
        items.add(new GridItem("Instagram", new Instagram(this, mMainModel.getFeedientService(), accessToken)));
        items.add(new GridItem("Youtube", new YouTube(this, mMainModel.getFeedientService(), accessToken)));
        items.add(new GridItem("Tumblr", new Tumblr(this, mMainModel.getFeedientService(), accessToken)));

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

    public ItemArrayAdapter getmItemArrayAdapter() {
        return mItemArrayAdapter;
    }

    public void setmItemArrayAdapter(ItemArrayAdapter mItemArrayAdapter) {
        this.mItemArrayAdapter = mItemArrayAdapter;
    }

    @Override
    public void onRefreshStarted(View view) {
        mMainModel.loadNewPosts();
    }
}
