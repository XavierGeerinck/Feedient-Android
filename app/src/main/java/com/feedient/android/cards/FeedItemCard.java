package com.feedient.android.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.models.json.schema.FeedPost;
import com.feedient.android.models.json.schema.entities.ExtendedLinkEntity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import it.gmariotti.cardslib.library.internal.Card;

public class FeedItemCard extends Card {
    // Statics
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    // Variables
    private FeedPost feedPost;
    private Drawable image;

    // Default constructor
    public FeedItemCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public FeedItemCard(Context context) {
        this(context, R.layout.list_feed_post_item);
    }

    public FeedItemCard(Context context, FeedPost feedPost) {
        this(context, R.layout.list_feed_post_item);

        this.feedPost = feedPost;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, final View view) {
        super.setupInnerViewElements(parent, view);

        // Header
        ImageView imgThumbnailUser  = (ImageView)view.findViewById(R.id.img_thumbnail_user);
        TextView txtUserPostedBy    = (TextView)view.findViewById(R.id.txt_user_posted_by);
        TextView txtDatePosted      = (TextView)view.findViewById(R.id.txt_date_posted);

        // Content
        TextView txtMessage         = (TextView)view.findViewById(R.id.txt_message);

        // Picture Entity
        LinearLayout containerPictureEntity = (LinearLayout)view.findViewById(R.id.container_entity_picture);
        ImageView imgEntityPicture  = (ImageView)view.findViewById(R.id.img_entity_picture);

        // Link Entity
        RelativeLayout containerLinkEntity       = (RelativeLayout)view.findViewById(R.id.container_entity_link);
        TextView txtEntityExtendedLinkTitle      = (TextView)view.findViewById(R.id.txt_entity_extended_link_title);
        TextView txtEntityExtendedLinkUrl        = (TextView)view.findViewById(R.id.txt_entity_extended_link_url);
        TextView txtEntityExtendedLinkDesc       = (TextView)view.findViewById(R.id.txt_entity_extended_link_description);
        ImageView imgEntityExtendedLinkThumbnail = (ImageView)view.findViewById(R.id.img_entity_extended_link_thumbnail);

        txtDatePosted.setText(DATE_FORMAT.format(feedPost.getContent().getDateCreated()));
        txtMessage.setText(feedPost.getContent().getMessage());
        txtUserPostedBy.setText(feedPost.getUser().getName());

        // Disable the entities from viewing by default
        containerLinkEntity.setVisibility(View.GONE);
        containerPictureEntity.setVisibility(View.GONE);

        // Load the image async
        Picasso.with(getContext()).load(feedPost.getUser().getImageLink()).into(imgThumbnailUser);

        // ENTITIES PARSING
        // Pictures (@todo: Add all the pictures, can be done after google released their CardView)
        if (feedPost.getContent().getEntities().getPictures().size() > 0) {
            containerLinkEntity.setVisibility(View.VISIBLE);
            imgEntityPicture.setImageDrawable(null);
            Picasso.with(getContext()).load(feedPost.getContent().getEntities().getPictures().get(0).getLargePictureUrl()).into(imgEntityPicture);
        }

        // Links (@todo: Add all the links, can be done after google released their CardView)

        // Extended Link
        if (!feedPost.getContent().getEntities().getExtendedLink().getName().equals("")) {
            containerPictureEntity.setVisibility(View.VISIBLE);
            ExtendedLinkEntity le = feedPost.getContent().getEntities().getExtendedLink();
            txtEntityExtendedLinkTitle.setText(le.getName());
            txtEntityExtendedLinkDesc.setText(le.getDescription());
            txtEntityExtendedLinkUrl.setText(le.getUrl());
            Picasso.with(getContext()).load(le.getImageUrl()).into(imgEntityExtendedLinkThumbnail);
        }
    }

    public FeedPost getFeedPost() {
        return feedPost;
    }

    public void setFeedPost(FeedPost feedPost) {
        this.feedPost = feedPost;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
