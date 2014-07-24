package com.feedient.compose.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.helpers.ProviderHelper;
import com.feedient.core.interfaces.FeedientService;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.models.json.UserProvider;
import com.feedient.core.models.json.response.PostMessage;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ComposeModel extends Observable {
    private SharedPreferences sharedPreferences;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private Context context;
    private FeedientService feedientService;

    private HashMap<String, IProviderModel> providers;
    private final List<UserProvider> userProviders;
    private final String accessToken;
    private SparseBooleanArray selectedUserProviders;
    private String selectedCameraImage;

    public ComposeModel(Context context, String accessToken, List<UserProvider> userProviders) {
        this.context = context;
        this.assetsPropertyReader = new AssetsPropertyReader(context);
        this.properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        this.sharedPreferences = context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        this.feedientService = new FeedientRestAdapter(context).getService();

        this.userProviders = userProviders;
        this.accessToken = accessToken;
        this.feedientService = new FeedientRestAdapter(context).getService();
        this.providers = ProviderHelper.getProviders(context, feedientService, accessToken);
        this.selectedUserProviders = new SparseBooleanArray();
        this.selectedCameraImage = "";
    }

    private void _triggerObservers() {
        setChanged();
        notifyObservers();
    }

    public void saveSelectedUserProviders(SparseBooleanArray selectedUserProviders) {
        this.selectedUserProviders = selectedUserProviders;
        Set<String> providerIds = new HashSet<String>();

        for (int i = 0; i < userProviders.size(); i++) {
            if (selectedUserProviders.get(i)) {
                providerIds.add(userProviders.get(i).getId());
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("compose_selected_user_providers", providerIds);
        editor.apply();
    }

    public void postMessage(String message) {
        String providerIds = getSelectedProvidersAsCommaString().toString();
        feedientService.postMessage(getAccessToken(), providerIds, message, new Callback<PostMessage[]>() {
            @Override
            public void success(PostMessage[] postMessages, Response response) {
                Log.e("Feedient", "SUCCESS");
            }


            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public void postMessageWithPicture(String message, TypedFile picture) {
        String providerIds = getSelectedProvidersAsCommaString().toString();
        Log.e("Feedient", providerIds);
        feedientService.postMessageWithPicture(getAccessToken(), providerIds, message, picture, new Callback<PostMessage[]>() {
            @Override
            public void success(PostMessage[] postMessages, Response response) {
                Log.e("Feedient", "SUCCESS");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("Feedient", retrofitError.getMessage());
            }
        });
    }

    public HashMap<String, IProviderModel> getProviders() {
        return providers;
    }

    public List<UserProvider> getUserProviders() {
        return userProviders;
    }

    public Set<String> getSelectedUserProviderIds() {
        return sharedPreferences.getStringSet("compose_selected_user_providers", new HashSet<String>());
    }

    public SparseBooleanArray getSelectedUserProviders() {
        return selectedUserProviders;
    }

    public String getSelectedCameraImage() {
        return selectedCameraImage;
    }

    public void setSelectedCameraImage(String selectedCameraImage) {
        this.selectedCameraImage = selectedCameraImage;
    }

    public JSONArray getSelectedProvidersAsCommaString() {
        JSONArray array = new JSONArray();

        for (String userProviderId : getSelectedUserProviderIds()) {
            array.put(userProviderId);
        }

        return array;
    }

    public String getAccessToken() {
        return sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");
    }
}
