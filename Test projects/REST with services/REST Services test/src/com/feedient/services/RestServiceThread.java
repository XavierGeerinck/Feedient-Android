package com.feedient.services;

import android.util.Log;
import com.feedient.pojo.Weather;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class RestServiceThread implements Callable<Weather> {

    private String url;

    public RestServiceThread(String url) {
        this.url = url;
    }

    @Override
    public Weather call() throws Exception {
        InputStream source = retrieveStream(url);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);
        return gson.fromJson(reader, Weather.class);
    }

    /* External code */
    private InputStream retrieveStream(String url) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
    }

}
