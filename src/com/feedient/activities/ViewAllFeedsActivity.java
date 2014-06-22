package com.feedient.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.data.AssetsPropertyReader;
import com.feedient.interfaces.FeedientService;
import com.feedient.interfaces.IViewAllFeeds;
import com.feedient.models.ViewAllFeeds;
import com.feedient.models.json.schema.FeedPost;
import com.feedient.models.json.UserProvider;
import com.feedient.tasks.SocketTask;
import org.json.JSONArray;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class ViewAllFeedsActivity extends ListActivity implements Observer {
    private ItemArrayAdapter itemArrayAdapter;
    private ViewAllFeeds viewAllFeeds;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_all_feeds); // @todo: When loading, set a loading icon

        viewAllFeeds = new ViewAllFeeds(this);
        viewAllFeeds.addObserver(this);
        viewAllFeeds.loadFeeds();

        itemArrayAdapter = new ItemArrayAdapter(this, viewAllFeeds.getFeedPosts());
        setListAdapter(itemArrayAdapter);
    }

    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        itemArrayAdapter.notifyDataSetChanged();
    }

    public ItemArrayAdapter getItemArrayAdapter() {
        return itemArrayAdapter;
    }

    public void setItemArrayAdapter(ItemArrayAdapter itemArrayAdapter) {
        this.itemArrayAdapter = itemArrayAdapter;
    }
}
