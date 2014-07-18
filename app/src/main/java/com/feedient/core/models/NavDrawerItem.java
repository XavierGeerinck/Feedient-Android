package com.feedient.core.models;

public class NavDrawerItem {
    private String title;
    private String icon;

    public NavDrawerItem() {
        this.title = "";
        this.icon = "";
    }

    public NavDrawerItem(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
