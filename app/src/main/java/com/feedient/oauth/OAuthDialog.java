package com.feedient.oauth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.oauth.webview.SocialWebViewClient;
import com.feedient.oauth.webview.WebViewCallback;

public class OAuthDialog extends Dialog {
    private Context context;
    private String dialogTitle;
    private int dialogTitleBackgroundColor;
    private String oAuthUrl;
    private String oAuthCallbackUrl;
    private WebViewCallback callback;

    private static final int paddingLeftAndRight = 0;
    private static final int paddingTopAndBot = 0;

    // Android components
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTxtTitle;
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public OAuthDialog(Context context, String oAuthUrl, String oAuthCallbackUrl, WebViewCallback callback) {
        super(context);

        this.context = context;
        this.oAuthCallbackUrl = oAuthCallbackUrl;
        this.oAuthUrl = oAuthUrl;
        this.callback = callback;
        this.dialogTitle = "";
        this.dialogTitleBackgroundColor = Color.parseColor("#1EAD91");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        mSpinner.setCancelable(true);

        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);

        setUpTitle();
        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final float scale = getContext().getResources().getDisplayMetrics().density;

        addContentView(mContent, new LinearLayout.LayoutParams(size.x - ((int) (paddingLeftAndRight * scale + 0.5f)), size.y - ((int) (paddingTopAndBot * scale + 0.5f))));
    }

    private void setUpTitle() {
        mTxtTitle = new TextView(getContext());
        mTxtTitle.setText(dialogTitle);
        mTxtTitle.setTextColor(Color.WHITE);
        mTxtTitle.setPadding(20, 15, 0, 15);
        mTxtTitle.setTextSize(20);
        mTxtTitle.setHeight(72);
        mTxtTitle.setBackgroundColor(this.dialogTitleBackgroundColor);

        mContent.addView(mTxtTitle);
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

    @Override
    public void setTitle(CharSequence title) {
        this.dialogTitle = title.toString();
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public int getDialogTitleBackgroundColor() {
        return dialogTitleBackgroundColor;
    }

    public void setDialogTitleBackgroundColor(int dialogTitleBackgroundColor) {
        this.dialogTitleBackgroundColor = dialogTitleBackgroundColor;
    }
}
