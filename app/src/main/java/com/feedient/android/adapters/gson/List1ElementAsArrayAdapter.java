package com.feedient.android.adapters.gson;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class List1ElementAsArrayAdapter implements JsonSerializer<List<?>> {
    @Override
    public JsonElement serialize(List<?> list, Type type, JsonSerializationContext jsonSerializationContext) {
        if (list.size() == 1) {
            // Don't put single element lists in a json array
            return new Gson().toJsonTree(list.get(0));
        } else {
            return new Gson().toJsonTree(list);
        }
    }
}
