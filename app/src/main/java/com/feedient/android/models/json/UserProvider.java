package com.feedient.android.models.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserProvider {
    @SerializedName("id")
    private String id;

    @SerializedName("order")
    private int order;

    @SerializedName("date_added")
    private Date dateAdded;

    @SerializedName("provider")
    private ProviderAccount providerAccount;

    public UserProvider() {
        id = "";
        order = 0;
        dateAdded = new Date();
        providerAccount = new ProviderAccount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public ProviderAccount getProviderAccount() {
        return providerAccount;
    }

    public void setProviderAccount(ProviderAccount providerAccount) {
        this.providerAccount = providerAccount;
    }
}
