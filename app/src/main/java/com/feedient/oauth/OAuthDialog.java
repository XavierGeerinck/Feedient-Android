package com.feedient.oauth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.feedient.oauth.webview.SocialWebViewClient;
import com.feedient.oauth.webview.WebViewCallback;

public class OAuthDialog extends Dialog {
    public static float width = 40;
    public static float height = 60;

    public static final float[] DIMENSIONS_DIFF_LANDSCAPE = { width, height };
    public static final float[] DIMENSIONS_DIFF_PORTRAIT = { width, height };

    private Context context;
    private String oAuthUrl;
    private String oAuthCallbackUrl;
    private WebViewCallback callback;

    // Android components
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public OAuthDialog(Context context, String oAuthUrl, String oAuthCallbackUrl, WebViewCallback callback) {
        super(context);

        this.context = context;
        this.oAuthCallbackUrl = oAuthCallbackUrl;
        this.oAuthUrl = oAuthUrl;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        mSpinner.setCancelable(true);

        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);

        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int orientation = getContext().getResources().getConfiguration().orientation;
        float[] dimensions = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? DIMENSIONS_DIFF_LANDSCAPE : DIMENSIONS_DIFF_PORTRAIT;

        addContentView(mContent, new LinearLayout.LayoutParams(size.x - ((int) (dimensions[0] * scale + 0.5f)), size.y - ((int) (dimensions[1] * scale + 0.5f))));
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new SocialWebViewClient(this, oAuthUrl, oAuthCallbackUrl, callback));
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(this.oAuthUrl);
        mWebView.setLayoutParams(FILL);

        mContent.addView(mWebView);
    }
}
