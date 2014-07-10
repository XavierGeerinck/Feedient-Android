package com.feedient.android.models.json.schema;

import com.feedient.android.models.json.schema.entities.ExtendedLinkEntity;
import com.feedient.android.models.json.schema.entities.ExtendedVideoEntity;
import com.feedient.android.models.json.schema.entities.HashtagEntity;
import com.feedient.android.models.json.schema.entities.LinkEntity;
import com.feedient.android.models.json.schema.entities.MentionEntity;
import com.feedient.android.models.json.schema.entities.PictureEntity;
import com.feedient.android.models.json.schema.entities.PlaceEntity;
import com.feedient.android.models.json.schema.entities.VideoEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Entities {
    @SerializedName("links")
    private List<LinkEntity> links;

    @SerializedName("pictures")
    private List<PictureEntity> pictures;

    @SerializedName("hashtags")
    private List<HashtagEntity> hashtags;

    @SerializedName("videos")
    private List<VideoEntity> videos;

    @SerializedName("mentions")
    private List<MentionEntity> mentions;

    @SerializedName("place")
    private PlaceEntity place;

    @SerializedName("extended_link")
    private ExtendedLinkEntity extendedLink;

    @SerializedName("extended_video")
    private ExtendedVideoEntity extendedVideo;

    public Entities() {
        this.links = new ArrayList<LinkEntity>();
        this.pictures = new ArrayList<PictureEntity>();
        this.hashtags = new ArrayList<HashtagEntity>();
        this.videos = new ArrayList<VideoEntity>();
        this.mentions = new ArrayList<MentionEntity>();
        this.place = new PlaceEntity();

        this.extendedLink = null;
        this.extendedVideo = null;
    }

    public List<LinkEntity> getLinks() {
        return links;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public List<HashtagEntity> getHashtags() {
        return hashtags;
    }

    public List<VideoEntity> getVideos() {
        return videos;
    }

    public List<MentionEntity> getMentions() {
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
