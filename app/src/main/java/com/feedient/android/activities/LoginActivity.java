package com.feedient.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.feedient.android.R;
import com.feedient.android.adapters.FeedientRestAdapter;
import com.feedient.android.data.AssetsPropertyReader;
import com.feedient.android.interfaces.FeedientService;
import com.feedient.android.models.json.UserSession;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.Properties;

public class LoginActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private FeedientService feedientService;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;

    // View Objects
    private EditText email;
    private EditText password;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        assetsPropertyReader = new AssetsPropertyReader(this);
        properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        sharedPreferences = getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        // If accessToken is set, then we are logged in (AutoLogin)
        if (!sharedPreferences.getString(properties.getProperty("prefs.key.token"), "").equals("")) {
            openViewAllFeedsActivity();
            return;
        }

        // Load components from view
        email = (EditText) findViewById(R.id.txtLoginEmail);
        password = (EditText) findViewById(R.id.txtLoginPassword);

        // Load service
        feedientService = new FeedientRestAdapter(this).getService();
    }

    public void login(View view) {
        Toast.makeText(getApplicationContext(), "Calling URL with: " + email.getText().toString(), Toast.LENGTH_LONG).show();

        feedientService.authorizeUser(email.getText().toString(), password.getText().toString(), new Callback<UserSession>() {
            @Override
            public void success(UserSession userSession, Response response) {
                // Save the accessToken and UID
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(properties.getProperty("prefs.key.token"), userSession.getToken());
                editor.putString(properties.getProperty("prefs.key.uid"), userSession.getUid());
                editor.commit();

                openViewAllFeedsActivity();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public void openViewAllFeedsActivity() {
        // Load the ViewAllFeeds activity
        Intent intent = new Intent(LoginActivity.this, ViewAllFeedsActivity.class);
        startActivity(intent);
    }
}
