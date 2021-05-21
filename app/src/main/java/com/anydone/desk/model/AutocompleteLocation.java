package com.anydone.desk.model;

public class AutocompleteLocation {
    private String primary;
    private String secondary;
    private double lat;
    private double lng;

    public AutocompleteLocation() {
    }

    public String getPrimary() {
        return primary;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}
