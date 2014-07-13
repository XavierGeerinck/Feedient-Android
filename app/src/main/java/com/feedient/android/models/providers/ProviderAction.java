package com.feedient.android.models.providers;

public class ProviderAction {
    private String nameNormal;
    private String namePerformed;
    private String icon;

    public ProviderAction(String nameNormal, String namePerformed, String icon) {
        this.nameNormal = nameNormal;
        this.namePerformed = namePerformed;
        this.icon = icon;
    }

    public String getNameNormal() {
        return nameNormal;
    }

    public String getNamePerformed() {
        return namePerformed;
    }

    public String getIcon() {
        return icon;
    }
}
