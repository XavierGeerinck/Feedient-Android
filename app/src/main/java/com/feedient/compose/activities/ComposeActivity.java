package com.feedient.compose.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.IconTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feedient.compose.adapters.UserProviderListAdapter;
import com.feedient.core.R;
import com.feedient.compose.models.ComposeModel;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.layout.FlowLayout;
import com.feedient.core.models.json.UserProvider;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

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

        FlowLayout containerSelectedProviders = (FlowLayout)findViewById(R.id.container_selected_providers);

        Set<String> selectedUserProviderIds = composeModel.getSelectedUserProviderIds();

        // Load the selected user providers in the containerHeader
        for (int i = 0; i < composeModel.getUserProviders().size(); i++) {
            // If userprovider is selected
            if (selectedUserProviderIds.contains(composeModel.getUserProviders().get(i).getId())) {
                UserProvider userProvider = composeModel.getUserProviders().get(i);

                View v = inflater.inflate(R.layout.compose_selected_user_provider, null);

                IconTextView providerIcon = (IconTextView) v.findViewById(R.id.img_user_provider_icon);
                TextView providerUserName = (TextView) v.findViewById(R.id.txt_provider_user_name);

                String providerName = userProvider.getProviderAccount().getName();
                providerIcon.setText("{" + composeModel.getProviders().get(providerName).getIcon() + "}");
                providerUserName.setText(userProvider.getProviderAccount().getUsername());

                if (TextUtils.isEmpty(userProvider.getProviderAccount().getUsername())) {
                    providerUserName.setText(userProvider.getProviderAccount().getFullName());
                }

                containerSelectedProviders.addView(v);
            }
        }
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
            View view = inflater.inflate(R.layout.view_select_user_provider, null);
//            view.startAnimation(AnimationUtils.loadAnimation(ComposeActivity.this, android.R.anim.fade_in));

            // Set view
            ComposeActivity.this.setContentView(view);

            // Set content
            final ListView listView = (ListView)findViewById(R.id.container_user_providers);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(new UserProviderListAdapter(ComposeActivity.this, composeModel.getUserProviders(), composeModel.getProviders()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    composeModel.saveSelectedUserProviders(listView.getCheckedItemPositions());
                }
            });

            // Select items
            Set<String> selectedUserProviderIds = composeModel.getSelectedUserProviderIds();

            // Load the selected user providers in the containerHeader
            for (int i = 0; i < composeModel.getUserProviders().size(); i++) {
                if (selectedUserProviderIds.contains(composeModel.getUserProviders().get(i).getId())) {
                    listView.setItemChecked(i, true);
                }
            }

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
