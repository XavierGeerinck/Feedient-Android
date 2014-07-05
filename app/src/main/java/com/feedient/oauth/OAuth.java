package com.feedient.oauth;

import android.content.Context;

public class OAuth {
    private Context mContext;

    public OAuth(Context context) {
        this.mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
