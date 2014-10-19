package com.feedient.core.model;

import java.util.Date;

public class Panel {
    private String id;
    private String workspaceId;
    private String name;
    private int type;
    private int size;
    private Date dateAdded;
    private String[] providerAccounts;
    private int order;

    public Panel() {
    }

    public String getId() {
        return id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String[] getProviderAccounts() {
        return providerAccounts;
    }

    public int getOrder() {
        return order;
    }
}
