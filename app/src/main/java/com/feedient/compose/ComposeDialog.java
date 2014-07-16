package com.feedient.compose;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feedient.android.R;

public class ComposeDialog extends Dialog {
    private Context context;
    private LayoutInflater inflater;

    private static final int paddingLeftAndRight = 0;
    private static final int paddingTopAndBot = 0;

    // Android components
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public ComposeDialog(Context context) {
        super(context);

        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_compose, null);


        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final float scale = getContext().getResources().getDisplayMetrics().density;

        addContentView(v, new LinearLayout.LayoutParams(size.x - ((int) (paddingLeftAndRight * scale + 0.5f)), size.y - ((int) (paddingTopAndBot * scale + 0.5f))));
    }
}