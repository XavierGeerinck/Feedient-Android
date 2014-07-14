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
import rx.functions.Action1;

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
        setContentView(R.layout.view_login);

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
        feedientService.authorizeUser(email.getText().toString(), password.getText().toString())
            .subscribe(new Action1<UserSession>() {
                @Override
                public void call(UserSession userSession) {
                    // Save the accessToken and UID
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(properties.getProperty("prefs.key.token"), userSession.getToken());
                    editor.putString(properties.getProperty("prefs.key.uid"), userSession.getUid());
                    editor.apply();

                    openViewAllFeedsActivity();
                }
            });
    }

    public void openViewAllFeedsActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // close current intent
    }
}
