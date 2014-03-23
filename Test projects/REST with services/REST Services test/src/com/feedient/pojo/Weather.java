package com.feedient.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: M
 * Date: 23/03/14
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class Weather {
    @SerializedName("coord")
    private Coordinates coordinates;
    private Sys sys;
    @SerializedName("weather")
    private ArrayList<Info> infos;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public ArrayList<Info> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<Info> infos) {
        this.infos = infos;
    }
}
