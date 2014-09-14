package com.feedient.compose.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feedient.compose.adapters.ImageGridAdapter;
import com.feedient.compose.adapters.UserProviderListAdapter;
import com.feedient.R;
import com.feedient.compose.models.ComposeModel;
import com.feedient.core.helpers.ImageLoaderHelper;
import com.feedient.core.helpers.KeyboardHelper;
import com.feedient.core.layout.FlowLayout;
import com.feedient.core.models.json.UserProvider;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import retrofit.mime.TypedFile;

public class ComposeActivity extends Activity implements Observer {
    private ComposeModel composeModel;
    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        this.composeModel = new ComposeModel(this, bundle.getString("accessToken"), (List<UserProvider>)bundle.getSerializable("userProviders"));
        this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        _showCompose();
    }

    private void _showCompose() {
        setContentView(R.layout.view_compose);

        // OnHeader click
        RelativeLayout containerHeader = (RelativeLayout)findViewById(R.id.container_header);
        containerHeader.setOnClickListener(new OnClickSelectUserProviders());

        // OnCamera click
        final IconTextView imgCamera = (IconTextView)findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(new OnClickSelectImage());

        // OnBtnSend click
        IconTextView btnSend = (IconTextView)findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new OnClickSendMessage());

        // Attach providers to header
        FlowLayout containerSelectedProviders = (FlowLayout)findViewById(R.id.container_selected_providers);

        // If we selected an image, hide camera and show the image
        if (!TextUtils.isEmpty(composeModel.getSelectedCameraImage())) {
            imgCamera.setVisibility(View.GONE);

            final RelativeLayout containerSelectedImage = (RelativeLayout)findViewById(R.id.container_selected_image);
            ImageView previewSelectedImage = (ImageView)findViewById(R.id.img_selected_image);
            IconTextView btnRemoveSelectedImage = (IconTextView)findViewById(R.id.btn_remove_selected_image);

            containerSelectedImage.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load(new File(composeModel.getSelectedCameraImage()))
                    .resize(100, 100)
                    .into(previewSelectedImage);

            containerSelectedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    containerSelectedImage.setVisibility(View.GONE);
                    imgCamera.setVisibility(View.VISIBLE);
                }
            });
        }

        // Listen on text changed
        EditText message = (EditText)findViewById(R.id.txt_message);
        message.setText(composeModel.getMessage());
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                composeModel.setMessage(editable.toString());
            }
        });

        Set<String> selectedUserProviderIds = composeModel.getSelectedUserProviderIds();

        // Load the selected user providers in the containerHeader
        for (int i = 0; i < composeModel.getUserProviders().size(); i++) {
            if (selectedUserProviderIds.contains(composeModel.getUserProviders().get(i).getId())) {
                UserProvider userProvider = composeModel.getUserProviders().get(i);

                View v = inflater.inflate(R.layout.frame_compose_selected_user_provider, null);

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

    private void _showSelectUserProviders() {
        // Hide keyboard
        KeyboardHelper.hideSoftKeyboard(ComposeActivity.this);

        // Create view + attach animation
        View view = inflater.inflate(R.layout.view_compose_select_user_provider, null);

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

    private void _showSelectImage() {
        // Hide keyboard
        KeyboardHelper.hideSoftKeyboard(ComposeActivity.this);

        // Create view + attach animation
        View view = inflater.inflate(R.layout.view_compose_select_image, null);

        // Set view
        ComposeActivity.this.setContentView(view);

        // Get images into grid
        final GridView gridView = (GridView)findViewById(R.id.grid_images);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        gridView.setAdapter(new ImageGridAdapter(ComposeActivity.this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                composeModel.setSelectedCameraImage(ImageLoaderHelper.getCameraImages(getApplicationContext()).get(pos));
                _showCompose();
            }
        });

        // Bind listener
        LinearLayout containerMessagePlaceholder = (LinearLayout)ComposeActivity.this.findViewById(R.id.container_header);
        containerMessagePlaceholder.setOnClickListener(new OnClickHeader());
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    private class OnClickSelectImage implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            _showSelectImage();
        }
    }

    private class OnClickSelectUserProviders implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            _showSelectUserProviders();
        }
    }

    private class OnClickHeader implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            _showCompose();
        }
    }

    private class OnClickSendMessage implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e("Feedient", "Sending message");
            EditText txtMessage = (EditText)findViewById(R.id.txt_message);

            if (!TextUtils.isEmpty(composeModel.getSelectedCameraImage())) {
                TypedFile picture = new TypedFile("multipart/form-data", new File(composeModel.getSelectedCameraImage()));
                composeModel.postMessageWithPicture(txtMessage.getText().toString(), picture);
            } else {
                composeModel.postMessage(txtMessage.getText().toString());
            }

            finish();
        }
    }
}
