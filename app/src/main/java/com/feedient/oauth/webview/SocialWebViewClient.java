package com.feedient.oauth.webview;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feedient.oauth.OAuthDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SocialWebViewClient extends WebViewClient {
    private String oAuthUrl;
    private String oAuthCallbackUrl;
    private HashMap<String, String> responseTokens;
    private WebViewCallback webViewCallback;
    private OAuthDialog dialog;

    public SocialWebViewClient(OAuthDialog dialog, String oAuthUrl, String oAuthCallbackUrl, WebViewCallback webViewCallback) {
        super();

        this.responseTokens = new HashMap<String, String>();
        this.oAuthUrl = oAuthUrl;
        this.oAuthCallbackUrl = oAuthCallbackUrl;
        this.webViewCallback = webViewCallback;
        this.dialog = dialog;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, oAuthUrl, favicon);

        // If the new url is our redirectUrl, then stop and try to extract the OAuth parameters
        if (url.startsWith(oAuthCallbackUrl)) {
            // Get parameters
            String[] parsedUrl = url.split("\\?");

            // Map tokens to fragments
            String[] tokens = parsedUrl[1].split("&");
            //removeElement(tokens, 0); // Remove first item

            // Add the tokens + value to our hashmap
            for (final String token : tokens) {
                final String tokenName = token.split("=")[0];
                final String tokenValue = token.split("=")[1];

                Log.e("Feedient", "Token: " + tokenName + " : " + tokenValue);
                responseTokens.put(tokenName, tokenValue);
            }

            // call our callback
            webViewCallback.onGotTokens(dialog, responseTokens);
        }
    }

    public void removeElement(Object[] a, int del) {
        System.arraycopy(a,del+1,a,del,a.length-1-del);
    }
}
