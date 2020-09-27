package com.treeleaf.januswebrtc;

public class Joinee {

    public Joinee(String name, String profileUrl, String accountId) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.accountId = accountId;
    }

    private String name, profileUrl;
    private String accountId;
    private boolean isDrawing = false;
    private Integer drawColor = -16777216;
    private boolean soloDrawing = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setDrawing(boolean drawing) {
        this.isDrawing = drawing;
    }

    public Integer getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(Integer drawColor) {
        this.drawColor = drawColor;
    }

    public boolean isSoloDrawing() {
        return soloDrawing;
    }

    public void setSoloDrawing(boolean soloDrawing) {
        this.soloDrawing = soloDrawing;
    }

}
