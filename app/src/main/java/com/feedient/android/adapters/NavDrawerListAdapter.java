package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.IconTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.feedient.android.R;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.NavDrawerItem;
import com.feedient.android.models.json.UserProvider;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavDrawerListAdapter extends BaseAdapter {
    private final Context context;
    private final List<NavDrawerItem> navDrawerItems;
    private final LayoutInflater inflater;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    public static class ViewHolderItem {
        IconTextView txtIcon;
        TextView txtTitle;
    }

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return navDrawerItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txt_title);
            viewHolder.txtIcon = (IconTextView)convertView.findViewById(R.id.txt_icon);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        viewHolder.txtTitle.setText(navDrawerItems.get(position).getTitle());
        viewHolder.txtIcon.setText(navDrawerItems.get(position).getIcon());
        Iconify.addIcons(viewHolder.txtIcon);

        return convertView;
    }
}
