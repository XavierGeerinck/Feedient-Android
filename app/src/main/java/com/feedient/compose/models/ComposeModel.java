package com.feedient.compose.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;

import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.helpers.ProviderHelper;
import com.feedient.core.interfaces.FeedientService;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.models.json.UserProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

public class ComposeModel extends Observable {
    private SharedPreferences sharedPreferences;
    private AssetsPropertyReader assetsPropertyReader;
    private Properties properties;
    private Context context;

    private HashMap<String, IProviderModel> providers;
    private FeedientService feedientService;
    private final List<UserProvider> userProviders;
    private final String accessToken;
    private SparseBooleanArray selectedUserProviders;

    public ComposeModel(Context context, String accessToken, List<UserProvider> userProviders) {
        this.context = context;
        this.assetsPropertyReader = new AssetsPropertyReader(context);
        this.properties = assetsPropertyReader.getProperties("shared_preferences.properties");
        this.sharedPreferences = context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        this.userProviders = userProviders;
        this.accessToken = accessToken;
        this.feedientService = new FeedientRestAdapter(context).getService();
        this.providers = ProviderHelper.getProviders(context, feedientService, accessToken);
        this.selectedUserProviders = new SparseBooleanArray();
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
}
