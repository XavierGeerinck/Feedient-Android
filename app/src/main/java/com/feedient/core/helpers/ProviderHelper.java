package com.feedient.core.helpers;

import android.content.Context;

import com.feedient.core.api.FeedientService;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.models.providers.*;

import java.util.HashMap;

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

    public static HashMap<String, IProviderModel> getProviders(Context context, FeedientService feedientService, String accessToken) {
        HashMap<String, IProviderModel> providers = new HashMap<String, IProviderModel>();
        providers.put("facebook", new Facebook(context, feedientService, accessToken));
        providers.put("twitter", new Twitter(context, feedientService, accessToken));
        providers.put("instagram", new Instagram(context, feedientService, accessToken));
        providers.put("youtube", new YouTube(context, feedientService, accessToken));
        providers.put("tumblr", new Tumblr(context, feedientService, accessToken));

        return providers;
    }
}
