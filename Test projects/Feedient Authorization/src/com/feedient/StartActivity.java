package com.feedient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.feedient.networking.Authorization;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class StartActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        final Context context = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String pword = password.getText().toString();

                Authorization auth = new Authorization(context);
                auth.execute(uname, pword);
                try {
                    String authkey = auth.get();
                    Toast.makeText(context, authkey, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    Log.d("Interrupted", e.getMessage());
                } catch (ExecutionException e) {
                    Log.d("Execution exception", e.getMessage());
                }


            }
        });
    }
}