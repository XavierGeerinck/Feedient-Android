package com.feedient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.feedient.R;
import com.feedient.adapters.FeedientRestAdapter;
import com.feedient.interfaces.FeedientService;
import com.feedient.models.UserSession;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {
    FeedientService feedientService;

    EditText email;
    EditText password;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Load components from view
        email     = (EditText)findViewById(R.id.txtLoginEmail);
        password  = (EditText)findViewById(R.id.txtLoginPassword);

        // Load service
        feedientService = new FeedientRestAdapter().getService();
    }

    public void login(View view) {
        Toast.makeText(getApplicationContext(), "Calling URL with: " + email.getText().toString(), Toast.LENGTH_LONG).show();

        feedientService.authorizeUser(email.getText().toString(), password.getText().toString(), new Callback<UserSession>() {
            @Override
            public void success(UserSession userSession, Response response) {
                Toast.makeText(getApplicationContext(), "Token: " + userSession.getToken(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });

    }
}
