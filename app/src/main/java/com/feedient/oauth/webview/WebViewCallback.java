package com.feedient.oauth.webview;

import android.app.Dialog;

import java.util.HashMap;

public interface WebViewCallback {
    public void onGotTokens(Dialog oAuthDialog, HashMap<String, String> tokens);
}
