package com.feedient.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.feedient.pojo.Weather;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class RestService extends Service {
    private final IBinder mBinder = new MyBinder();
    private Context context;

    public IBinder onBind(Intent intent) {
        context = getApplicationContext();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RestService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("RestService destroyed", "");

    }

    public class MyBinder extends Binder {
        public RestService getService() {
            return RestService.this;
        }
    }

    public Weather retrieveRestData() throws ExecutionException, InterruptedException {
        Callable<Weather> callable = new RestServiceThread("http://api.openweathermap.org/data/2.5/weather?id=2172797");
        FutureTask<Weather> futureTask = new FutureTask<Weather>(callable);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(futureTask);
        if (!futureTask.isDone()) {
            Intent i = new Intent();
            i.setAction("PROGRESS_UPDATE");
            i.setFlags(1);
            context.sendBroadcast(i);
        }
        return futureTask.get();
    }
}
