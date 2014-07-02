package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.feedient.android.R;
import com.feedient.android.helpers.ImageLoaderHelper;
import com.feedient.android.helpers.ProviderHelper;
import com.feedient.android.interfaces.IProviderModel;
import com.feedient.android.models.json.UserProvider;
import com.joanzapata.android.iconify.Iconify;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class DrawerItemAdapter extends ArrayAdapter<UserProvider> {
    private final Context context;
    private final List<UserProvider> userProviders;
    private final ImageLoader imageLoader;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    static class ViewHolderItem {
        ImageView imgProviderIcon;
        TextView txtProviderIcon;
        TextView txtProviderUserName;
    }

    public DrawerItemAdapter(Context context, List<UserProvider> userProviders) {
        super(context, R.layout.view_main, userProviders);

        this.context = context;
        this.userProviders = userProviders;
        this.imageLoader = ImageLoaderHelper.getImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            // Inflate the layout
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.imgProviderIcon = (ImageView)convertView.findViewById(R.id.img_provider_icon);
            viewHolder.txtProviderUserName = (TextView)convertView.findViewById(R.id.txt_provider_user_name);
            viewHolder.txtProviderIcon = (TextView)convertView.findViewById(R.id.txt_provider_icon);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        UserProvider userProvider = userProviders.get(position);

        if (userProvider != null) {
            IProviderModel providerModel = ProviderHelper.providerNameToClass(userProvider.getProviderAccount().getName());

            if (providerModel != null) {
                viewHolder.txtProviderIcon.setText("{" + providerModel.getIcon() + "}");
            }

            if (userProvider.getProviderAccount().getUsername() != null) {
                viewHolder.txtProviderUserName.setText(userProvider.getProviderAccount().getUsername());
            }

            if (userProvider.getProviderAccount().getFullName() != null) {
                viewHolder.txtProviderUserName.setText(userProvider.getProviderAccount().getFullName());
            }

            Iconify.addIcons(viewHolder.txtProviderIcon);
        }

        return convertView;
    }
}
