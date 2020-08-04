package com.treeleaf.anydone.serviceprovider.realm.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Service extends RealmObject implements Parcelable {
    public static final String SERVICE_ID = "serviceId";
    @PrimaryKey
    private String serviceId;
    private String name;
    private String desc;
    private String serviceType;
    private String serviceIconUrl;
    private long createdAt;
    private RealmList<ServiceAttributes> serviceAttributesList;

    public Service() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceIconUrl() {
        return serviceIconUrl;
    }

    public void setServiceIconUrl(String serviceIconUrl) {
        this.serviceIconUrl = serviceIconUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public RealmList<ServiceAttributes> getServiceAttributesList() {
        return serviceAttributesList;
    }

    public void setServiceAttributesList(RealmList<ServiceAttributes> serviceAttributesList) {
        this.serviceAttributesList = serviceAttributesList;
    }

    protected Service(Parcel in) {
        serviceId = in.readString();
        name = in.readString();
        desc = in.readString();
        serviceType = in.readString();
        serviceIconUrl = in.readString();
        createdAt = in.readLong();
        if (in.readByte() == 0x01) {
            serviceAttributesList = new RealmList<ServiceAttributes>();
            in.readList(serviceAttributesList, ServiceAttributes.class.getClassLoader());
        } else {
            serviceAttributesList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceId);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(serviceType);
        dest.writeString(serviceIconUrl);
        dest.writeLong(createdAt);
        if (serviceAttributesList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(serviceAttributesList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}