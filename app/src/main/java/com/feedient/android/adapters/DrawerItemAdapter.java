package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.feedient.android.R;
import com.feedient.android.helpers.ImageLoaderHelper;
import com.feedient.android.models.json.UserProvider;
import com.feedient.android.models.json.schema.FeedPost;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

public class DrawerItemAdapter extends ArrayAdapter<UserProvider> {
    private final Context context;
    private final List<UserProvider> userProviders;
    private final ImageLoader imageLoader;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    static class ViewHolderItem {
        ImageView imgProviderIcon;
        IconTextView txtProviderUserName;
    }

    public DrawerItemAdapter(Context context, List<UserProvider> userProviders) {
        super(context, R.layout.view_all_feeds, userProviders);

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
            convertView = inflater.inflate(R.layout.feed_list_row, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.imgProviderIcon = (ImageView)convertView.findViewById(R.id.img_provider_icon);
            viewHolder.txtProviderUserName = (IconTextView)convertView.findViewById(R.id.txt_provider_user_name);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        UserProvider userProvider = userProviders.get(position);

        if (userProvider != null) {
            viewHolder.txtProviderUserName.setText(userProvider.getProviderAccount().getUsername());

            // Load the image async
            //imageLoader.displayImage(feedPost.getUser().getImageLink(), (ImageView)viewHolder.imgThumbnailUser);
        }

        return convertView;
    }
}
