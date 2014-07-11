package com.feedient.android.models;

public class NavDrawerItem {
    private String title;
    private String icon;
    private boolean isRemoveVisible = false;

    public NavDrawerItem() {
        this.title = "";
        this.icon = "";
    }

    public NavDrawerItem(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, String icon, boolean isRemoveVisible) {
        this.title = title;
        this.icon = icon;
        this.isRemoveVisible = isRemoveVisible;
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

    public boolean isRemoveVisible() {
        return isRemoveVisible;
    }

    public void setRemoveVisible(boolean isRemoveVisible) {
        this.isRemoveVisible = isRemoveVisible;
    }
}
