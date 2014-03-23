package com.feedient.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class Coordinates {
    @SerializedName("lon")
    private double longitude;
    @SerializedName("lat")
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
