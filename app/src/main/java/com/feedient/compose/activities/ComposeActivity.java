package com.feedient.compose.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.feedient.core.R;
import com.feedient.core.adapters.GridItemUserProviderAdapter;
import com.feedient.compose.models.ComposeModel;
import com.feedient.core.models.json.UserProvider;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ComposeActivity extends Activity implements Observer {
    private ComposeModel composeModel;

    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        this.composeModel = new ComposeModel(this, bundle.getString("accessToken"), (List<UserProvider>)bundle.getSerializable("userProviders"));
        this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        showCompose();
    }

    private void showCompose() {
        setContentView(R.layout.view_compose);
        RelativeLayout containerHeader = (RelativeLayout)findViewById(R.id.container_header);
        containerHeader.setOnClickListener(new OnClickSelectUserProviders());
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    private class OnClickSelectUserProviders implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showSelectUserProviders();
        }

        private void showSelectUserProviders() {
            // Create view + attach animation
            View view = inflater.inflate(R.layout.dialog_select_user_provider, null);
//            view.startAnimation(AnimationUtils.loadAnimation(ComposeActivity.this, android.R.anim.fade_in));

            // Set view
            ComposeActivity.this.setContentView(view);

            // Set content
            GridView gridView = (GridView)findViewById(R.id.container_user_providers);
            gridView.setAdapter(new GridItemUserProviderAdapter(ComposeActivity.this, composeModel.getUserProviders(), composeModel.getProviders()));

            // Bind listener
            LinearLayout containerMessagePlaceholder = (LinearLayout)ComposeActivity.this.findViewById(R.id.container_header);
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
