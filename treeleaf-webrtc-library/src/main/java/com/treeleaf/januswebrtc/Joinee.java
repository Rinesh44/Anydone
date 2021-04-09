package com.treeleaf.januswebrtc;

import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Picture;

import java.util.LinkedHashMap;

public class Joinee {

    public Joinee(String name, String profileUrl, String accountId, boolean selfAccount) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.accountId = accountId;
        this.selfAccount = selfAccount;
    }

    private String name, profileUrl;
    private String accountId;
    private boolean isDrawing = false;
    private Integer drawColor = -16777216;
    private boolean soloDrawing = false;
    private Picture currentWatchingPicture;
    private boolean selfAccount;
    private String currentImageId;
    private LinkedHashMap<String, JoineeDrawState> mapImageDrawState = new LinkedHashMap<>();

    /**
     * default image state is close since no image is available at start
     */
    private JoineeDrawState joineeDrawStateLocal = JoineeDrawState.CLOSED;
    private JoineeDrawState joineeDrawStateRemote = JoineeDrawState.CLOSED;

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

    public Picture getCurrentWatchingPicture() {
        return currentWatchingPicture;
    }

    public void setCurrentWatchingPicture(Picture currentWatchingPicture) {
        this.currentWatchingPicture = currentWatchingPicture;
    }

    public JoineeDrawState getJoineeDrawStateLocal() {
        return joineeDrawStateLocal;
    }

    public void setJoineeDrawStateLocal(JoineeDrawState joineeDrawStateLocal) {
        this.joineeDrawStateLocal = joineeDrawStateLocal;
    }

    public JoineeDrawState getJoineeDrawStateRemote() {
        return joineeDrawStateRemote;
    }

    public void setJoineeDrawStateRemote(JoineeDrawState joineeDrawStateRemote) {
        this.joineeDrawStateRemote = joineeDrawStateRemote;
    }

    public boolean isSelfAccount() {
        return selfAccount;
    }

    public void setSelfAccount(boolean selfAccount) {
        this.selfAccount = selfAccount;
    }

    public String getCurrentImageId() {
        return currentImageId;
    }

    public void setCurrentImageId(String currentImageId) {
        this.currentImageId = currentImageId;
    }

    public LinkedHashMap<String, JoineeDrawState> getMapImageDrawState() {
        return mapImageDrawState;
    }

    public void setMapImageDrawState(LinkedHashMap<String, JoineeDrawState> mapImageDrawState) {
        this.mapImageDrawState = mapImageDrawState;
    }

    public enum JoineeDrawState {
        MINIMIZED, MAXIMIZED, CLOSED
    }

}
