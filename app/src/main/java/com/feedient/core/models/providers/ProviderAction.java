package com.feedient.core.models.providers;

import com.feedient.core.interfaces.ISocialActionCallback;

public class ProviderAction {
    private String nameNormal;
    private String namePerformed;
    private String icon;
    private ISocialActionCallback callback;

    public ProviderAction(String nameNormal, String namePerformed, String icon, ISocialActionCallback callback) {
        this.nameNormal = nameNormal;
        this.namePerformed = namePerformed;
        this.icon = icon;
        this.callback = callback;
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

    public ISocialActionCallback getCallback() {
        return callback;
    }
}
