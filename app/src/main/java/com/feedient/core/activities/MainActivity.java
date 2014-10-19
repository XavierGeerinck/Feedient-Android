package com.feedient.core.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
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

import com.feedient.compose.activities.ComposeActivity;
import com.feedient.core.R;
import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.adapters.NavDrawerListAdapter;
import com.feedient.core.adapters.FeedListAdapter;
import com.feedient.core.adapters.GridItemAdapter;
import com.feedient.core.adapters.PanelsAdapter;
import com.feedient.core.api.FeedientService;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.interfaces.IDrawerPanelItemCallback;
import com.feedient.core.interfaces.ILoadMoreListener;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.listeners.LoadMoreListener;
import com.feedient.core.model.Panel;
import com.feedient.core.models.GridItem;
import com.feedient.core.models.MainModel;

import com.feedient.core.models.NavDrawerItem;
import com.feedient.core.models.json.UserProvider;
import com.feedient.core.views.FloatingActionButton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class MainActivity extends Activity {
    // Drawer variables
    private NavDrawerListAdapter mNavDrawerListAdapter;
    private PanelsAdapter mPanelsAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mNavDrawerList;
    private ListView mPanelsList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Feed variables
    private FeedListAdapter mFeedListAdapter;
    //private MainModel mMainModel;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mFeedPostsList;
    private FloatingActionButton mBtnCompose;

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
        mFeedPostsList          = (ListView)findViewById(R.id.list);
        mDrawerLayout           = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavDrawerList          = (ListView)findViewById(R.id.drawer_list);
        mPanelsList             = (ListView)findViewById(R.id.drawer_provider_list);
        //mBtnCompose             = (FloatingActionButton)findViewById(R.id.btn_compose);

        // Services
        assetsPropertyReader = new AssetsPropertyReader(getApplicationContext());
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        configProperties = assetsPropertyReader.getProperties("config.properties");
        sharedPreferences = getApplicationContext().getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        feedientService = new FeedientRestAdapter(getApplicationContext()).getService();

        // Variables
        accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");

        // Default methods
        loadData();

        // Adapters
        initDrawerMenuItems();

        // Init the views

//
//        mBtnCompose.setColor(Color.WHITE);
//        mBtnCompose.setDrawable(R.drawable.ic_compose);
//        mBtnCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickCompose();
//            }
//        });
//
//        // Fix margin top and bottom for list of cards
//        mFeedPostsList.addFooterView(new View(this), null, false);
//        mFeedPostsList.addHeaderView(new View(this), null, false);
//
//        // Set the adapter for our feed
//        mFeedListAdapter = new FeedListAdapter(this, mMainModel.getFeedPosts(), mMainModel.getUserProviders(), mMainModel.getProviders());
//        mFeedPostsList.setAdapter(mFeedListAdapter);
//        mFeedPostsList.setOnScrollListener(new LoadMoreListener(new ILoadMoreListener() {
//            @Override
//            public void onScrollCompleted() {
//                if (!mMainModel.isLoadingOlderPosts()) {
//                    mMainModel.loadOlderPosts();
//                }
//            }
//        }));

        // Set the adapter for our drawer list navigation items




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
        //mNavDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//        mPullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.swipe_container);
//        ActionBarPullToRefresh.from(this)
//                .allChildrenArePullable()
//                .listener(this)
//                .setup(mPullToRefreshLayout);

        // Start auto update
        //mMainModel.initAutoUpdateTimer();
    }

    private void initDrawerMenuItems() {
        String[] navMenuTitles = getApplicationContext().getResources().getStringArray(R.array.nav_drawer_items);
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], "{fa-plus}")); // Add provider
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], "{fa-sign-out}")); // Sign Out

        mNavDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mNavDrawerList.setAdapter(mNavDrawerListAdapter);
    }

    private void loadData() {
        feedientService.getAccount(accessToken, new Callback<FeedientService.UserResponse>() {
            @Override
            public void success(FeedientService.UserResponse userResponse, Response response) {
                // Set the adapter for our drawer provider list items
                mPanelsAdapter = new PanelsAdapter(getApplicationContext(), userResponse.getWorkspaces().get(0).getPanels(), new IDrawerPanelItemCallback() {
                    @Override
                    public void onClickRemovePanel(Panel p) {
                        MainActivity.this.onClickRemovePanel(p);
                    }
                });

                mPanelsList.setAdapter(mPanelsAdapter);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
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


//    @Override
//    public void onRefreshStarted(View view) {
//        mMainModel.loadNewPosts();
//    }


//    @Override
//    public void update(Observable observable, Object o) {
//        // Notify our list that there are updates
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                checkLoggedIn();
//
//                mFeedListAdapter.notifyDataSetChanged();
//                mNavDrawerListAdapter.notifyDataSetChanged();
//                mPanelsAdapter.notifyDataSetChanged();
//                mPullToRefreshLayout.setRefreshing(mMainModel.isRefreshing());
//            }
//        });
//    }

//    private void checkLoggedIn() {
//        if (mMainModel.getAccessToken() == null || mMainModel.getAccessToken().equals("")) {
//            openLoginActivity();
//        }
//    }

    private void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // close current intent
    }

//    private void openComposeActivity() {
//        Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
//        intent.putExtra("userProviders", (Serializable)mMainModel.getUserProviders());
//        intent.putExtra("accessToken", mMainModel.getAccessToken());
//        startActivity(intent);
//    }

    public void onClickRemovePanel(final Panel p) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_remove_user_provider_title)
                .setMessage(R.string.dialog_remove_user_provider_message)
                .setPositiveButton(R.string.choice_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //mMainModel.removeUserProvider(p);
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

//    private void onClickCompose() {
////        ComposeDialog dialog = new ComposeDialog(this, mMainModel.getUserProviders(), mMainModel.getProviders());
////        dialog.show();
//
//        openComposeActivity();
//    }

//    /**
//     * DrawerItemListener -  OnItemClick in the drawer
//     */
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            executeAction(position);
//        }
//    }

//    private void executeAction(int position) {
//        View view = getWindow().getDecorView().getRootView();
//
//        switch (position) {
//            // Add Provider
//            case 0:
//                actionAddUserProvider(view);
//                break;
//            // Logout
//            case 1:
//                actionLogout(view);
//                break;
//        }
//    }

//    public void actionAddUserProvider(View v) {
//        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_grid, null);
//        final List<GridItem> items = new ArrayList<GridItem>();
//
//        for (IProviderModel provider : mMainModel.getProviders().values()) {
//            items.add(new GridItem(provider.getName(), provider));
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(customView);
//
//        final AlertDialog pickProviderDialog = builder.create();
//        pickProviderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pickProviderDialog.show();
//
//        GridView gridView = (GridView)customView.findViewById(R.id.gridview);
//        gridView.setAdapter(new GridItemAdapter(this, items));
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // call the OAuth popup
//                mMainModel.addUserProvider(items.get(position).getProviderModel());
//
//                // Close the other dialog
//                pickProviderDialog.dismiss();
//            }
//        });
//    }
//
//    public void actionLogout(final View v) {
//        new AlertDialog.Builder(this)
//                .setTitle(R.string.dialog_sign_out_title)
//                .setMessage(R.string.dialog_sign_out_message)
//                .setPositiveButton(R.string.choice_confirm, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mMainModel.logout();
//                    }
//                })
//                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                })
//                .show();
//    }
}
