package com.feedient.core.model;

import java.util.Date;

public class Workspace {
    private String id;
    private String name;
    private String creator;
    private Date dateAdded;
    private String[] users;
    private Panel[] panels;

    public Workspace() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String[] getUsers() {
        return users;
    }

    public Panel[] getPanels() {
        return panels;
    }
}
