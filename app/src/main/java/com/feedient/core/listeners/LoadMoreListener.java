package com.feedient.core.listeners;

import android.widget.AbsListView;

import com.feedient.core.interfaces.ILoadMoreListener;

public class LoadMoreListener implements AbsListView.OnScrollListener {
    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private ILoadMoreListener callback;

    public LoadMoreListener(ILoadMoreListener callback) {
        this.callback = callback;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.currentScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;

        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && this.currentScrollState != SCROLL_STATE_IDLE) {
            callback.onScrollCompleted();
        }
    }
}
