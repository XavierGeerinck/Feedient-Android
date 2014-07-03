package com.feedient.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feedient.android.R;

import org.json.JSONException;
import org.json.JSONObject;

public class OAuthActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent thisIntent = getIntent();
        final String oauthUrl = thisIntent.getStringExtra("OAUTH_URL");
        final String redirectUrl = thisIntent.getStringExtra("OAUTH_REDIRECT_URI");
        final String providerName = thisIntent.getStringExtra("OAUTH_PROVIDER_NAME");
        final String[] oauthFragments = thisIntent.getStringArrayExtra("OAUTH_FRAGMENTS");

        setContentView(R.layout.view_oauth);

        WebView webview = (WebView)findViewById(R.id.webview_oauth);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // If the new url is our redirectUrl, then stop and try to extract the OAuth parameters
                if (url.startsWith(redirectUrl)) {
                    JSONObject jo = new JSONObject();

                    // Map tokens to fragments
                    String[] tokens = url.split("[\\?&][a-z_#]+=");
                    removeElement(tokens, 0); // Remove first item

                    for (int i = 0; i < tokens.length; i++) {
                        if (oauthFragments.length > i) {
                            try {
                                // Remove gibberish
                                tokens[i] = tokens[i].replaceAll("#.+", "");
                                jo.put(oauthFragments[i], tokens[i]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Log.e("Feedient", jo.toString());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("data", jo.toString());
                    returnIntent.putExtra("provider", providerName);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        webview.loadUrl(oauthUrl);
    }

    public void removeElement(Object[] a, int del) {
        System.arraycopy(a,del+1,a,del,a.length-1-del);
    }
}
