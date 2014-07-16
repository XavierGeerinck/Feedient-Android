package com.feedient.android.adapters;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
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
import com.feedient.android.interfaces.IDrawerProviderItemCallback;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.NavDrawerItem;
import com.feedient.android.models.json.UserProvider;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavDrawerProvidersListAdapter extends BaseAdapter {
    private final Context context;
    private final HashMap<String, IProviderModel> providers;
    private final List<UserProvider> userProviders;
    private final LayoutInflater inflater;
    private final IDrawerProviderItemCallback callback;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    public static class ViewHolderItem {
        UserProvider userProvider;
        IconTextView txtProviderIcon;
        TextView txtProviderUserName;
        ImageButton txtRemoveIcon;
    }

    public NavDrawerProvidersListAdapter(Context context, List<UserProvider> userProviders, HashMap<String, IProviderModel> providers, IDrawerProviderItemCallback callback) {
        this.context = context;
        this.userProviders = userProviders;
        this.providers = providers;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return userProviders.size();
    }

    @Override
    public Object getItem(int i) {
        return userProviders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_provider_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();

            viewHolder.txtProviderIcon = (IconTextView)convertView.findViewById(R.id.txt_provider_icon);
            viewHolder.txtProviderUserName = (TextView)convertView.findViewById(R.id.txt_provider_user_name);
            viewHolder.txtRemoveIcon = (ImageButton)convertView.findViewById(R.id.img_btn_provider_remove);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        if (userProviders.size() > position) {
            viewHolder.userProvider = userProviders.get(position);
        }

        if (viewHolder.userProvider != null) {
            if (viewHolder.userProvider.getProviderAccount() != null) {
                viewHolder.txtProviderIcon.setText("{" + providers.get(viewHolder.userProvider.getProviderAccount().getName().toLowerCase()).getIcon() + "}");

                if (viewHolder.userProvider.getProviderAccount().getFullName() != null && !TextUtils.isEmpty(viewHolder.userProvider.getProviderAccount().getFullName())) {
                    viewHolder.txtProviderUserName.setText(viewHolder.userProvider.getProviderAccount().getFullName());
                }

                if (viewHolder.userProvider.getProviderAccount().getUsername() != null && !TextUtils.isEmpty(viewHolder.userProvider.getProviderAccount().getUsername())) {
                    viewHolder.txtProviderUserName.setText(viewHolder.userProvider.getProviderAccount().getUsername());
                }
            }

            Iconify.addIcons(viewHolder.txtProviderIcon);
        }

        viewHolder.txtRemoveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickRemoveUserProvider(viewHolder.userProvider);
            }
        });

        return convertView;
    }
}
