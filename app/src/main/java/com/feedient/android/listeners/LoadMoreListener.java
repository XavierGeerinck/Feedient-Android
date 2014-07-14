package com.feedient.android.listeners;

import android.widget.AbsListView;

import com.feedient.android.interfaces.ILoadMoreListener;

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
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
            // Check if scrolling is done
            callback.onScrollCompleted();
        }
    }
}
