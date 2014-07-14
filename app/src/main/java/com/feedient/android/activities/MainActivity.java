package com.feedient.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.feedient.android.R;
import com.feedient.android.adapters.NavDrawerListAdapter;
import com.feedient.android.adapters.FeedListAdapter;
import com.feedient.android.adapters.GridItemAdapter;
import com.feedient.android.adapters.NavDrawerProvidersListAdapter;
import com.feedient.android.interfaces.IDrawerProviderItemCallback;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.GridItem;
import com.feedient.android.models.MainModel;

import com.feedient.android.models.json.UserProvider;
import com.feedient.android.views.FloatingActionButton;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer, OnRefreshListener {
    // Drawer variables
    private NavDrawerListAdapter mNavDrawerListAdapter;
    private NavDrawerProvidersListAdapter mNavDrawerProvidersListAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mNavDrawerList;
    private ListView mNavDrawerProvidersList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Feed variables
    private FeedListAdapter mFeedListAdapter;
    private MainModel mMainModel;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mFeedPostsList;
    private FloatingActionButton mBtnCompose;

    // App title
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main); // @todo: When loading, set a loading icon

        mTitle = mDrawerTitle = getTitle();

        // Init observers
        mMainModel = new MainModel(this);
        mMainModel.addObserver(this);
        mMainModel.loadUser();
        mMainModel.loadFeeds();

        // Init the views
        mFeedPostsList          = (ListView)findViewById(R.id.list);
        mDrawerLayout           = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavDrawerList          = (ListView)findViewById(R.id.drawer_list);
        mNavDrawerProvidersList = (ListView)findViewById(R.id.drawer_provider_list);
        mBtnCompose             = (FloatingActionButton)findViewById(R.id.btn_compose);

        mBtnCompose.setColor(Color.WHITE);
        mBtnCompose.setDrawable(R.drawable.ic_compose);

        // Fix margin top and bottom for list of cards
        mFeedPostsList.addFooterView(new View(this), null, false);
        mFeedPostsList.addHeaderView(new View(this), null, false);

        // Set the adapter for our feed
        mFeedListAdapter = new FeedListAdapter(this, mMainModel.getFeedPosts(), mMainModel.getProviders());
        mFeedPostsList.setAdapter(mFeedListAdapter);

        // Set the adapter for our drawer list navigation items
        mNavDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), mMainModel.getNavDrawerItems());
        mNavDrawerList.setAdapter(mNavDrawerListAdapter);

        // Set the adapter for our drawer provider list items
        mNavDrawerProvidersListAdapter = new NavDrawerProvidersListAdapter(getApplicationContext(), mMainModel.getUserProviders(), mMainModel.getProviders(), new IDrawerProviderItemCallback() {
            @Override
            public void onClickRemoveUserProvider(UserProvider up) {
                MainActivity.this.onClickRemoveUserProvider(up);
            }
        });
        mNavDrawerProvidersList.setAdapter(mNavDrawerProvidersListAdapter);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
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

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Start auto update
        //mMainModel.initAutoUpdateTimer();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync toggle state after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onRefreshStarted(View view) {
        mMainModel.loadNewPosts();
    }


    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLoggedIn();

                mFeedListAdapter.notifyDataSetChanged();
                mNavDrawerListAdapter.notifyDataSetChanged();
                mNavDrawerProvidersListAdapter.notifyDataSetChanged();
                mPullToRefreshLayout.setRefreshing(mMainModel.isRefreshing());
            }
        });
    }

    private void checkLoggedIn() {
        if (mMainModel.getAccessToken() == null || mMainModel.getAccessToken().equals("")) {
            openLoginActivity();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close current intent
    }

    public void onClickRemoveUserProvider(final UserProvider up) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_remove_user_provider_title)
                .setMessage(R.string.dialog_remove_user_provider_message)
                .setPositiveButton(R.string.choice_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMainModel.removeUserProvider(up);
                        //mNavDrawerListAdapter.remove(up);
                    }
                })
                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
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
            // Add Provider
            case 0:
                actionAddUserProvider(view);
                break;
            // Logout
            case 1:
                actionLogout(view);
                break;
        }
    }

    public void actionAddUserProvider(View v) {
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_grid, null);
        final List<GridItem> items = new ArrayList<GridItem>();

        for (IProviderModel provider : mMainModel.getProviders().values()) {
            items.add(new GridItem(provider.getName(), provider));
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);

        final AlertDialog pickProviderDialog = builder.create();
        pickProviderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickProviderDialog.show();

        GridView gridView = (GridView)customView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridItemAdapter(this, items));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // call the OAuth popup
                mMainModel.addUserProvider(items.get(position).getProviderModel());

                // Close the other dialog
                pickProviderDialog.dismiss();
            }
        });
    }

    public void actionLogout(final View v) {
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
}
