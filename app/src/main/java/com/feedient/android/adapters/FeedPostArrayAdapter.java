package com.feedient.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.models.json.schema.FeedPost;
import com.feedient.android.models.json.schema.entities.ExtendedLinkEntity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class FeedPostArrayAdapter extends ArrayAdapter<FeedPost> {
    // Statics
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    // Variables
    private final Context context;
    private final List<FeedPost> feedPosts;

    // ViewHolder
    static class ViewHolder {
        // Header
        ImageView imgThumbnailUser ;
        TextView txtUserPostedBy;
        TextView txtDatePosted;

        // Content
        TextView txtMessage;

        // Entities Container
        LinearLayout containerEntities;
    }

    public FeedPostArrayAdapter(Context context, List<FeedPost> feedPosts) {
        super(context, R.layout.list_feed_post_item, feedPosts);
        this.context = context;
        this.feedPosts = feedPosts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Setup the items to re-use
        // Disable re-use, constantly re-use since we got dynamic adding of entities
        //if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_feed_post_item, null);

            // Configure ViewHolder
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgThumbnailUser = (ImageView)rowView.findViewById(R.id.img_thumbnail_user);
            viewHolder.txtUserPostedBy  = (TextView)rowView.findViewById(R.id.txt_user_posted_by);
            viewHolder.txtDatePosted    = (TextView)rowView.findViewById(R.id.txt_date_posted);
            viewHolder.txtMessage       = (TextView)rowView.findViewById(R.id.txt_message);

            // Set tag to rowView
            rowView.setTag(viewHolder);
        //}

        // Fill Data
        ViewHolder holder = (ViewHolder)rowView.getTag();
        holder.containerEntities = (LinearLayout)rowView.findViewById(R.id.layout_entities);

        FeedPost feedPost = feedPosts.get(position);

        holder.txtDatePosted.setText(DATE_FORMAT.format(feedPost.getContent().getDateCreated()));
        holder.txtMessage.setText(feedPost.getContent().getMessage());
        holder.txtUserPostedBy.setText(feedPost.getUser().getName());

        // Load the image async
        Picasso.with(getContext()).load(feedPost.getUser().getImageLink()).into(holder.imgThumbnailUser);

        // ENTITIES PARSING
        // Pictures
        if (feedPost.getContent().getEntities().getPictures().size() > 0) {
            _handleEntityPictures(inflater, holder.containerEntities, feedPost);
        }

        // Links (@todo)

        // Extended Link (@todo: better design)
        if (!feedPost.getContent().getEntities().getExtendedLink().getName().equals("")) {
            _handleEntityExtendedLink(inflater, holder.containerEntities, feedPost);
        }

        // Return view
        return rowView;
    }

    private void _handleEntityPictures(LayoutInflater inflater, LinearLayout container, FeedPost fp) {
        View entityPictureView = inflater.inflate(R.layout.entity_picture, null);

        // Init Elements
        ImageView imgEntityPicture = (ImageView)entityPictureView.findViewById(R.id.img_entity_picture);

        container.addView(entityPictureView);

        // Init Data
        Picasso.with(getContext()).load(fp.getContent().getEntities().getPictures().get(0).getLargePictureUrl()).into(imgEntityPicture);
    }

    private void _handleEntityExtendedLink(LayoutInflater inflater, LinearLayout container, FeedPost fp) {
        View entityView = inflater.inflate(R.layout.entity_extended_link, null);

        // Init Elements
        ImageView imgEntityExtendedLinkThumbnail = (ImageView)entityView.findViewById(R.id.img_entity_extended_link_thumbnail);
        TextView txtEntityExtendedLinkTitle = (TextView)entityView.findViewById(R.id.txt_entity_extended_link_title);
        TextView txtEntityExtendedLinkDesc = (TextView)entityView.findViewById(R.id.txt_entity_extended_link_description);
        TextView txtEntityExtendedLinkUrl = (TextView)entityView.findViewById(R.id.txt_entity_extended_link_url);

        // Init data
        ExtendedLinkEntity le = fp.getContent().getEntities().getExtendedLink();
        txtEntityExtendedLinkTitle.setText(le.getName());
        txtEntityExtendedLinkDesc.setText(le.getDescription());
        txtEntityExtendedLinkUrl.setText(le.getUrl());

        container.addView(entityView);

        // Async load image
        Picasso.with(getContext()).load(le.getImageUrl()).into(imgEntityExtendedLinkThumbnail);
    }
}