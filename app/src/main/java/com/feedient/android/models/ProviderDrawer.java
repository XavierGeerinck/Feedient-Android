package com.feedient.android.models;

import android.content.Context;

import java.util.Observable;

public class ProviderDrawer extends Observable {
    private Context context;

    public ProviderDrawer(Context context) {
        this.context = context;
    }

    private void _triggerObservers() {
        setChanged();
        notifyObservers();
    }
}
