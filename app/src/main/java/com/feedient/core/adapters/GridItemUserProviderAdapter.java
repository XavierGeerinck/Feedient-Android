package com.feedient.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import com.feedient.core.R;
import com.feedient.core.interfaces.IProviderModel;
import com.feedient.core.models.json.UserProvider;

import java.util.HashMap;
import java.util.List;

public class GridItemUserProviderAdapter extends BaseAdapter {
    private final List<UserProvider> userProviders;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final HashMap<String, IProviderModel> providers;

    public GridItemUserProviderAdapter(Context context, List<UserProvider> userProviders, HashMap<String, IProviderModel> providers) {
        this.context = context;
        this.userProviders = userProviders;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.providers = providers;
    }

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    static class ViewHolderItem {
        IconTextView iconProvider;
        TextView username;
    }

    @Override
    public int getCount() {
        return userProviders.size();
    }

    @Override
    public Object getItem(int position) {
        return userProviders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userProviders.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_compose_user_provider, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.iconProvider = (IconTextView)convertView.findViewById(R.id.img_user_provider_icon);
            viewHolder.username = (TextView)convertView.findViewById(R.id.txt_panel_name);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        UserProvider up = userProviders.get(position);
        IProviderModel provider = providers.get(up.getProviderAccount().getName());

        if (up != null && provider != null) {
            viewHolder.iconProvider.setText("{" + provider.getIcon() + "}");
            viewHolder.username.setText(up.getProviderAccount().getUsername());
        }

        return convertView;
    }
}