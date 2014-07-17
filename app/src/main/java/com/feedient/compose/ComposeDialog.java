package com.feedient.compose;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.IconTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.adapters.GridItemAdapter;
import com.feedient.android.adapters.GridItemUserProviderAdapter;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.UserProvider;

import java.util.HashMap;
import java.util.List;

public class ComposeDialog extends Dialog {
    private Context context;
    private LayoutInflater inflater;
    private List<UserProvider> userProviders;
    private HashMap<String, IProviderModel> providers;

    private static final int paddingLeftAndRight = 10;
    private static final int paddingTopAndBot = 10;

    public ComposeDialog(Context context, List<UserProvider> userProviders, HashMap<String, IProviderModel> providers) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userProviders = userProviders;
        this.providers = providers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_compose);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        IconTextView btnSelectUserProviders = (IconTextView)findViewById(R.id.btn_select_user_providers);
        btnSelectUserProviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeDialog.this.setContentView(R.layout.dialog_select_user_provider);
                GridView gridView = (GridView)ComposeDialog.this.findViewById(R.id.container_user_providers);
                gridView.setAdapter(new GridItemUserProviderAdapter(context, userProviders, providers));
            }
        });
    }
}