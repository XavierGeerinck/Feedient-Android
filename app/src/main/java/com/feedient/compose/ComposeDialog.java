package com.feedient.compose;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.feedient.core.R;
import com.feedient.core.adapters.GridItemUserProviderAdapter;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.models.json.UserProvider;

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

        // Set full screen + transparent
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        showCompose();
    }

    private void showCompose() {
        // Set view
        ComposeDialog.this.setContentView(R.layout.view_compose);

        // Set content

        // Bind listener
        RelativeLayout containerHeader = (RelativeLayout)ComposeDialog.this.findViewById(R.id.container_header);
        containerHeader.setOnClickListener(new OnClickSelectUserProviders());
    }

    private class OnClickSelectUserProviders implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showSelectUserProviders();
        }

        private void showSelectUserProviders() {
            // Create view + attach animation
            View view = inflater.inflate(R.layout.dialog_select_user_provider, null);
            view.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));

            // Set view
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(10, 10, 10, 10);
            view.setLayoutParams(layoutParams);
            ComposeDialog.this.setContentView(view);

            // Set content
            GridView gridView = (GridView)findViewById(R.id.container_user_providers);
            gridView.setAdapter(new GridItemUserProviderAdapter(context, userProviders, providers));

            // Bind listener
            LinearLayout containerMessagePlaceholder = (LinearLayout)ComposeDialog.this.findViewById(R.id.container_header);
            containerMessagePlaceholder.setOnClickListener(new OnClickHeader());
        }
    }

    private class OnClickHeader implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showCompose();
        }
    }
}