package com.treeleaf.januswebrtc;

public class Joinee {

    public Joinee(String name, String profileUrl, String accountId) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.accountId = accountId;
    }

    private String name, profileUrl;
    private String accountId;

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
}
