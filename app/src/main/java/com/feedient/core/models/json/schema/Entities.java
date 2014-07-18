package com.feedient.core.models.json.schema;

import com.feedient.core.models.json.schema.entities.ExtendedLinkEntity;
import com.feedient.core.models.json.schema.entities.ExtendedVideoEntity;
import com.feedient.core.models.json.schema.entities.HashtagEntity;
import com.feedient.core.models.json.schema.entities.LinkEntity;
import com.feedient.core.models.json.schema.entities.MentionEntity;
import com.feedient.core.models.json.schema.entities.PictureEntity;
import com.feedient.core.models.json.schema.entities.PlaceEntity;
import com.feedient.core.models.json.schema.entities.VideoEntity;
import com.google.gson.annotations.SerializedName;

public class Entities {
    @SerializedName("links")
    private LinkEntity[] links;

    @SerializedName("pictures")
    private PictureEntity[] pictures;

    @SerializedName("hashtags")
    private HashtagEntity[] hashtags;

    @SerializedName("videos")
    private VideoEntity[] videos;

    @SerializedName("mentions")
    private MentionEntity[] mentions;

    @SerializedName("place")
    private PlaceEntity place;

    @SerializedName("extended_link")
    private ExtendedLinkEntity extendedLink;

    @SerializedName("extended_video")
    private ExtendedVideoEntity extendedVideo;

    public Entities() {
        this.place = new PlaceEntity();

        this.extendedLink = null;
        this.extendedVideo = null;
    }

    public LinkEntity[] getLinks() {
        return links;
    }

    public PictureEntity[] getPictures() {
        return pictures;
    }

    public HashtagEntity[] getHashtags() {
        return hashtags;
    }

    public VideoEntity[] getVideos() {
        return videos;
    }

    public MentionEntity[] getMentions() {
        return mentions;
    }

    public PlaceEntity getPlace() {
        return place;
    }

    public ExtendedLinkEntity getExtendedLink() {
        return extendedLink;
    }

    public ExtendedVideoEntity getExtendedVideo() {
        return extendedVideo;
    }
}
