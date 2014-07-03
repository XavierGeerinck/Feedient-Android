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
import com.feedient.android.models.json.schema.FeedPost;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<FeedPost> {
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    private final Context context;
    private final List<FeedPost> feedPosts;
    private final ImageLoader imageLoader;
    private final LayoutInflater layoutInflater;

    // ViewHolder pattern (http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder)
    static class ViewHolderItem {
        ImageView imgThumbnailUser;
        TextView txtUserPostedBy;
        TextView txtMessage;
        TextView txtDatePosted;
    }

    public ItemArrayAdapter(Context context, List<FeedPost> feedPosts) {
        super(context, R.layout.view_main, feedPosts);

        this.context = context;
        this.feedPosts = feedPosts;
        this.imageLoader = ImageLoaderHelper.getImageLoader(context);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.feed_list_item, null);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.imgThumbnailUser = (ImageView)convertView.findViewById(R.id.img_thumbnail_user);
            viewHolder.txtMessage = (TextView)convertView.findViewById(R.id.txt_message);
            viewHolder.txtUserPostedBy = (TextView)convertView.findViewById(R.id.txt_user_posted_by);
            viewHolder.txtDatePosted = (TextView)convertView.findViewById(R.id.txt_date_posted);

            // Store the holder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem)convertView.getTag();
        }

        FeedPost feedPost = feedPosts.get(position);

        if (feedPost != null) {
            viewHolder.txtMessage.setText(feedPost.getContent().getMessage());
            viewHolder.txtDatePosted.setText(DATE_FORMAT.format(feedPost.getContent().getDateCreated()));
            viewHolder.txtUserPostedBy.setText(feedPost.getUser().getName());

            // Load the image async
            imageLoader.displayImage(feedPost.getUser().getImageLink(), (ImageView)viewHolder.imgThumbnailUser);
        }

        return convertView;
    }
}
