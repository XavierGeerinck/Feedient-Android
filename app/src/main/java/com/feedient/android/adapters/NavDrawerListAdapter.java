package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    public static class ViewHolderItem {
        UserProvider userProvider;
        IconTextView txtIcon;
        TextView txtTitle;
        ImageButton txtRemoveIcon;
    }

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> navDrawerItems, HashMap<String, IProviderModel> providers) {
        this.context = context;
        this.providers = providers;
        this.navDrawerItems = navDrawerItems;
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.userProvider = userProviders.get(position);
            viewHolder.txtProviderUserName = (TextView)convertView.findViewById(R.id.txt_provider_user_name);
            viewHolder.txtProviderIcon = (TextView)convertView.findViewById(R.id.txt_provider_icon);
            viewHolder.imgBtnRemoveProvider = (ImageButton)convertView.findViewById(R.id.img_btn_provider_remove);
            viewHolder.imgBtnRemoveProvider.setTag(viewHolder.userProvider);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        if (viewHolder.userProvider != null) {
            if (viewHolder.userProvider.getProviderAccount() != null) {
                viewHolder.txtProviderIcon.setText("{" + providers.get(viewHolder.userProvider.getProviderAccount().getName().toLowerCase()).getIcon() + "}");
            }

            if (viewHolder.userProvider.getProviderAccount().getUsername() != null) {
                viewHolder.txtProviderUserName.setText(viewHolder.userProvider.getProviderAccount().getUsername());
            }

            if (viewHolder.userProvider.getProviderAccount().getFullName() != null) {
                viewHolder.txtProviderUserName.setText(viewHolder.userProvider.getProviderAccount().getFullName());
            }

            Iconify.addIcons(viewHolder.txtProviderIcon);
        }

        return convertView;
    }
}
