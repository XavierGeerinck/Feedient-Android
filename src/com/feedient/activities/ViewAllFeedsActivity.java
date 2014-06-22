package com.feedient.activities;

import android.app.ListActivity;
import android.os.Bundle;
import com.feedient.adapters.ItemArrayAdapter;
import com.feedient.models.ViewAllFeeds;

import java.util.Observable;
import java.util.Observer;

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


        viewAllFeeds.loadSocket();

        itemArrayAdapter = new ItemArrayAdapter(this, viewAllFeeds.getFeedPosts());
        setListAdapter(itemArrayAdapter);
    }

    @Override
    public void update(Observable observable, Object o) {
        // Notify our list that there are updates
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public ItemArrayAdapter getItemArrayAdapter() {
        return itemArrayAdapter;
    }

    public void setItemArrayAdapter(ItemArrayAdapter itemArrayAdapter) {
        this.itemArrayAdapter = itemArrayAdapter;
    }
}
