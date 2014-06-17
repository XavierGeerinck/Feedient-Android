package com.feedient.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
public class Authorization extends AsyncTask<String, Integer, String> {
    private ProgressDialog pd;

    public Authorization(Context context) {
        pd = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        pd.setMessage("Loading ...");
        pd.show();

    }

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        return getAuthorizationToken(username, password);
    }

    private String getAuthorizationToken(String username, String password) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.feedient.com/user/authorize");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            Log.d("authorization - ClientProtocolException", e.getMessage());
        } catch (IOException e) {
            Log.d("authorization - IOException", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
    }
}
