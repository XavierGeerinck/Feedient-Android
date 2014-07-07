package com.feedient.android.helpers;

import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.providers.*;

public class ProviderHelper {
    public static Class providerNameToClass(String providerName) {
        if (providerName.equalsIgnoreCase("facebook")) {
            return Facebook.class;
        }  else if (providerName.equalsIgnoreCase("twitter")) {
            return Twitter.class;
        }  else if (providerName.equalsIgnoreCase("instagram")) {
            return Instagram.class;
        }  else if (providerName.equalsIgnoreCase("youtube")) {
            return YouTube.class;
        } else if (providerName.equalsIgnoreCase("tumblr")) {
            return Tumblr.class;
        }

        return null;
    }
}
