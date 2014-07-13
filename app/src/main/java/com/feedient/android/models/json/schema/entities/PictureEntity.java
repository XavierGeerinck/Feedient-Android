package com.feedient.android.models.json.schema.entities;

import com.google.gson.annotations.SerializedName;

public class PictureEntity {
    @SerializedName("small_picture")
    private SmallPicture smallPicture;

    @SerializedName("large_picture")
    private LargePicture largePicture;

    @SerializedName("caption")
    private String caption; // Can be empty

    public PictureEntity() {
        this.smallPicture = new SmallPicture();
        this.largePicture = new LargePicture();
        this.caption = "";
    }

    public SmallPicture getSmallPicture() {
        return smallPicture;
    }

    public LargePicture getLargePicture() {
        return largePicture;
    }

    public String getCaption() {
        return caption;
    }

    public class SmallPicture {
        @SerializedName("url")
        private String url;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public SmallPicture() {
            url = "";
            width = 0;
            height = 0;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class LargePicture {
        @SerializedName("url")
        private String url;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public LargePicture() {
            url = "";
            width = 0;
            height = 0;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}

