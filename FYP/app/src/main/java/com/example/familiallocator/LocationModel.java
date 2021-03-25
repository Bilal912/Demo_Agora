package com.example.familiallocator;

public class LocationModel {

    public String lat,log;

    public LocationModel() {
    }

    public LocationModel(String lat, String log) {
        this.lat = lat;
        this.log = log;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
