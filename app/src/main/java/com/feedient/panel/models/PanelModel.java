package com.feedient.panel.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.feedient.R;
import com.feedient.core.adapters.FeedientRestAdapter;
import com.feedient.core.data.AssetsPropertyReader;
import com.feedient.core.interfaces.FeedientService;
import com.feedient.core.models.NavDrawerItem;
import com.feedient.core.models.json.response.Logout;
import com.feedient.core.pojo.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import rx.functions.Action1;

public class PanelModel extends Observable {
    private Context context;

    private List<NavDrawerItem> navDrawerMenuItems;
    private List<NavDrawerItem> navDrawerPanelItems;

    private Account account;
    private String accessToken;
    private FeedientService feedientService;

    public PanelModel(Context context) {
        // application
        this.context = context;

        // feedient
        Properties properties = new AssetsPropertyReader(context).getProperties("shared_preferences.properties");
        SharedPreferences sharedPreferences = context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(properties.getProperty("prefs.key.token"), "");
        this.feedientService = new FeedientRestAdapter(context).getService();
        account = new Account();

        // components
        this.initDrawerItems();
    }

    /**
     * Initiate the drawer items
     */
    private void initDrawerItems() {
        String[] navPanelEntries = context.getResources().getStringArray(R.array.nav_drawer_panel_items);
        navDrawerPanelItems = new ArrayList<NavDrawerItem>();

        navDrawerPanelItems.add(new NavDrawerItem(navPanelEntries[0], "{fa-list}")); // add Feed
        navDrawerPanelItems.add(new NavDrawerItem(navPanelEntries[1], "{fa-inbox}")); // add Inbox
        navDrawerPanelItems.add(new NavDrawerItem(navPanelEntries[2], "{fa-bar-chart-o}")); // add Analytics

        String[] navMenuEntries = context.getResources().getStringArray(R.array.nav_drawer_menu_items);
        navDrawerMenuItems = new ArrayList<NavDrawerItem>();

        navDrawerMenuItems.add(new NavDrawerItem(navMenuEntries[0], "{fa-gear}")); // add Settings
        navDrawerMenuItems.add(new NavDrawerItem(navMenuEntries[1], "{fa-sign-out}")); // add Sign Out
    }

    public void loadUser() {
        this.feedientService.getAccount(this.accessToken)
            .subscribe(new Action1<Account>() {
                    @Override
                    public void call(Account account) {
                        PanelModel.this.account.setId(account.getId());
                        PanelModel.this.account.setEmail(account.getEmail());
                        PanelModel.this.account.setLanguage(account.getLanguage());
                        PanelModel.this.account.setRole(account.getRole());

                        PanelModel.this.triggerObservers();
                    }
                });
    }

    /**
     * Sign out current user
     */
    public void signout() {
        feedientService.logout(this.accessToken)
                .subscribe(new Action1<Logout>() {
                    @Override
                    public void call(Logout logout) {
                        PanelModel.this.removeAccessToken();
                        PanelModel.this.triggerObservers();
                    }
                });
    }

    private void removeAccessToken() {
        Properties properties = new AssetsPropertyReader(this.context).getProperties("shared_preferences.properties");
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(properties.getProperty("prefs.name"), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(properties.getProperty("prefs.key.token"));
        editor.apply();
        this.accessToken = "";
    }

    private void triggerObservers() {
        setChanged();
        notifyObservers();
    }

    public String getAccessToken() { return accessToken; }
    public List<NavDrawerItem> getNavDrawerMenuItems() { return navDrawerMenuItems; }
    public List<NavDrawerItem> getNavDrawerPanelItems() { return navDrawerPanelItems; }
}
