package com.feedient.android.helpers;

import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.providers.*;

public class ProviderHelper {
    public static IProviderModel providerNameToClass(String providerName) {
        if (providerName.equalsIgnoreCase("facebook")) {
            return new Facebook();
        }  else if (providerName.equalsIgnoreCase("twitter")) {
            return new Twitter();
        }  else if (providerName.equalsIgnoreCase("instagram")) {
            return new Instagram();
        }  else if (providerName.equalsIgnoreCase("youtube")) {
            return new YouTube();
        } else if (providerName.equalsIgnoreCase("tumblr")) {
            return new Tumblr();
        }

        return null;
    }
}
