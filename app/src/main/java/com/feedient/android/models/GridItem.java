package com.feedient.android.models;

import android.widget.IconTextView;

public class GridItem {
    private String title;
    private String faIconText;

    public GridItem(String title, String faIconText) {
        this.title = title;
        this.faIconText = faIconText;
    }

    public String getFaIconText() {
        return faIconText;
    }

    public void setFaIconText(String faIconText) {
        this.faIconText = faIconText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
