package com.anydone.desk.inviteuserstocall;

import android.os.Parcel;
import android.os.Parcelable;

public class AddedParticipantsForCall implements Parcelable {
    public String accountId;
    public String employeeId;
    public String fullAccountName;
    public String profileUrl;

    public AddedParticipantsForCall() {

    }

    protected AddedParticipantsForCall(Parcel in) {
        accountId = in.readString();
        employeeId = in.readString();
        fullAccountName = in.readString();
        profileUrl = in.readString();
    }

    public static final Creator<AddedParticipantsForCall> CREATOR = new Creator<AddedParticipantsForCall>() {
        @Override
        public AddedParticipantsForCall createFromParcel(Parcel in) {
            return new AddedParticipantsForCall(in);
        }

        @Override
        public AddedParticipantsForCall[] newArray(int size) {
            return new AddedParticipantsForCall[size];
        }
    };

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullAccountName() {
        return fullAccountName;
    }

    public void setFullAccountName(String fullAccountName) {
        this.fullAccountName = fullAccountName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accountId);
        parcel.writeString(employeeId);
        parcel.writeString(fullAccountName);
        parcel.writeString(profileUrl);
    }
}