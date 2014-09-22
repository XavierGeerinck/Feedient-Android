package com.feedient.core.pojo;

import com.feedient.core.models.json.ProviderAccount;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Panel implements Serializable {
    @SerializedName("workspaceId")
    private String workspaceId;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("order")
    private String order;

    @SerializedName("providerAccounts")
    private List<ProviderAccount> providerAccounts;

    @SerializedName("dateAdded")
    private Date dateAdded;

    public Panel() {
        this.workspaceId = "";
        this.name = "";
        this.type = "";
        this.order = "";
        this.providerAccounts = new ArrayList<ProviderAccount>();
        this.dateAdded = new Date();
    }

    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }

    public List<ProviderAccount> getProviderAccounts() { return providerAccounts; }
    public void setProviderAccounts(List<ProviderAccount> providerAccounts) {
        this.providerAccounts = providerAccounts;
    }

    public Date getDateAdded() { return dateAdded; }
    public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }
}
