package com.project.accordo.Entity;


public class LocationPost extends Post{
    private String lat;
    private String lon;

    public LocationPost(String uid, String name, String pversion, String pid, String type) {
        super(uid, name, pversion, pid, type);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
