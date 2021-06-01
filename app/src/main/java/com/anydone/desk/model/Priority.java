package com.anydone.desk.model;

import io.realm.RealmObject;

public class Priority extends RealmObject {
    private String value;
    private int icon;

    public Priority() {
    }

    public Priority(String value, int icon) {
        this.value = value;
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
