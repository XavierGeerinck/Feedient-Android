package com.feedient.android.cards;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedient.android.R;
import com.feedient.android.helpers.ImageLoaderHelper;
import com.feedient.android.models.json.schema.FeedPost;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;

import it.gmariotti.cardslib.library.internal.Card;

public class FeedItemCard extends Card {
    // Statics
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    // Variables
    private FeedPost feedPost;
    private final ImageLoader imageLoader;

    // Android Components
    private ImageView imgThumbnailUser;
    private ImageView imgContent;
    private TextView txtUserPostedBy;
    private TextView txtMessage;
    private TextView txtDatePosted;

    // Default constructor
    public FeedItemCard(Context context, int innerLayout, ImageLoader imageLoader) {
        super(context, innerLayout);
        this.imageLoader = imageLoader;
    }

    public FeedItemCard(Context context, ImageLoader imageLoader) {
        this(context, R.layout.card_inner_content_feed_item, imageLoader);
    }

    public FeedItemCard(Context context, FeedPost feedPost, ImageLoader imageLoader) {
        this(context, R.layout.card_inner_content_feed_item, imageLoader);

        this.feedPost = feedPost;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);

        imgThumbnailUser = (ImageView)parent.findViewById(R.id.img_thumbnail_user);
        imgThumbnailUser.setImageDrawable(null);
        imgContent = (ImageView)parent.findViewById(R.id.img_message);
        imgContent.setImageDrawable(null);
        txtUserPostedBy = (TextView)parent.findViewById(R.id.txt_user_posted_by);
        txtMessage = (TextView)parent.findViewById(R.id.txt_message);
        txtDatePosted = (TextView)parent.findViewById(R.id.txt_date_posted);

        txtDatePosted.setText(DATE_FORMAT.format(feedPost.getContent().getDateCreated()));
        txtMessage.setText(feedPost.getContent().getMessage());
        txtUserPostedBy.setText(feedPost.getUser().getName());

        // ENTITIES PARSING
        // Pictures
        if (feedPost.getContent().getEntities().getPictures().size() > 0) {
            imageLoader.cancelDisplayTask(imgContent);
            imageLoader.displayImage(feedPost.getContent().getEntities().getPictures().get(0).getSmallPictureUrl(), imgContent);
        }

        // Load the image async
        imageLoader.displayImage(feedPost.getUser().getImageLink(), imgThumbnailUser);
    }

    public FeedPost getFeedPost() {
        return feedPost;
    }

    public ImageView getImgThumbnailUser() {
        return imgThumbnailUser;
    }

    public void setImgThumbnailUser(ImageView imgThumbnailUser) {
        this.imgThumbnailUser = imgThumbnailUser;
    }

    public TextView getTxtUserPostedBy() {
        return txtUserPostedBy;
    }

    public void setTxtUserPostedBy(TextView txtUserPostedBy) {
        this.txtUserPostedBy = txtUserPostedBy;
    }

    public TextView getTxtMessage() {
        return txtMessage;
    }

    public void setTxtMessage(TextView txtMessage) {
        this.txtMessage = txtMessage;
    }

    public TextView getTxtDatePosted() {
        return txtDatePosted;
    }

    public void setTxtDatePosted(TextView txtDatePosted) {
        this.txtDatePosted = txtDatePosted;
    }

    public void setFeedPost(FeedPost feedPost) {
        this.feedPost = feedPost;
    }
}