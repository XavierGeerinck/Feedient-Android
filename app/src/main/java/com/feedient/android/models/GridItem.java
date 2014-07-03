package com.feedient.android.models;

import android.widget.IconTextView;

import com.feedient.android.interfaces.IProviderModel;

public class GridItem {
    private String title;
    private IProviderModel providerModel;

    public GridItem(String title, IProviderModel providerModel) {
        this.title = title;
        this.providerModel = providerModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IProviderModel getProviderModel() {
        return providerModel;
    }

    public void setProviderModel(IProviderModel providerModel) {
        this.providerModel = providerModel;
    }
}
