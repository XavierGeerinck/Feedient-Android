package com.feedient;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.feedient.pojo.Weather;
import com.feedient.services.RestService;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class StartActivity extends Activity {
    private RestService restService;
    private boolean isConnected = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);

        Button b = (Button) findViewById(R.id.test);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weather w = null;
                if (isConnected) {
                    try {
                        w = restService.retrieveRestData();
                    } catch (ExecutionException e) {
                        Log.d("Data retrieval: Execution error", e.getMessage());
                    } catch (InterruptedException e) {
                        Log.d("Data retrieval: Interrupted error", e.getMessage());
                    }
                    if (w != null) {
                        TextView tv = (TextView) findViewById(R.id.tvTest);
                        StringBuilder s = new StringBuilder();
                        s.append("Message");
                        s.append("\n");
                        s.append("\t" + w.getSys().getMessage());
                        s.append("\n");
                        s.append("Country");
                        s.append("\n");
                        s.append("\t" + w.getSys().getCountry());
                        s.append("\n");
                        s.append("Longitude");
                        s.append("\n");
                        s.append("\t" + w.getCoordinates().getLongitude());
                        s.append("\n");
                        s.append("Latitude");
                        s.append("\n");
                        s.append("\t" + w.getCoordinates().getLatitude());
                        tv.setText(s.toString());
                    }
                }
            }
        });
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("PROGRESS_UPDATE")) {
                Toast.makeText(StartActivity.this, "fetching data", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
    // Flag if receiver is registered
    private boolean mReceiversRegistered = false;
    // Define a handler and a broadcast receiver
    private final Handler mHandler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        if (mReceiversRegistered) {
            unregisterReceiver(mIntentReceiver);
            mReceiversRegistered = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        if (mReceiversRegistered) {
            unregisterReceiver(mIntentReceiver);
            mReceiversRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, RestService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("PROGRESS_UPDATE");
        this.registerReceiver(mIntentReceiver, intentToReceiveFilter, null, mHandler);
        mReceiversRegistered = true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            RestService.MyBinder b = (RestService.MyBinder) binder;
            restService = b.getService();
            Toast.makeText(StartActivity.this, "Connected", Toast.LENGTH_SHORT)
                    .show();
            isConnected = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            restService = null;
            isConnected = false;
        }
    };
}