package com.feedient.core.pojo;

import com.feedient.core.models.json.ProviderAccount;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Panel implements Serializable {
    @SerializedName("workspaceId")
    public String workspaceId;

    @SerializedName("name")
    public String name;

    @SerializedName("type")
    public String type;

    @SerializedName("order")
    public String order;

    @SerializedName("providerAccounts")
    public List<ProviderAccount> providerAccounts;

    @SerializedName("dateAdded")
    public Date dateAdded;

    public Panel() {

    }
}
