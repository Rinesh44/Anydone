package com.treeleaf.anydone.serviceprovider.realm.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class ServiceAttributes extends RealmObject implements Parcelable {
    public static final String SERVICE_ATTRIBUTE_ID = "serviceAttributeId";
    private String serviceId;
    private String name;
    private String serviceAttributeType;
    private long createdAt;
    private String value;
    private long date;

    public ServiceAttributes() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceAttributeType() {
        return serviceAttributeType;
    }

    public void setServiceAttributeType(String serviceAttributeType) {
        this.serviceAttributeType = serviceAttributeType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private ServiceAttributes(Parcel in) {
        serviceId = in.readString();
        name = in.readString();
        serviceAttributeType = in.readString();
        createdAt = in.readLong();
        value = in.readString();
        date = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceId);
        dest.writeString(name);
        dest.writeString(serviceAttributeType);
        dest.writeLong(createdAt);
        dest.writeString(value);
        dest.writeLong(date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ServiceAttributes> CREATOR = new Parcelable.Creator<ServiceAttributes>() {
        @Override
        public ServiceAttributes createFromParcel(Parcel in) {
            return new ServiceAttributes(in);
        }

        @Override
        public ServiceAttributes[] newArray(int size) {
            return new ServiceAttributes[size];
        }
    };
}