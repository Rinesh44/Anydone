package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KGraph extends RealmObject {
    @PrimaryKey
    private String id;
    private String prev;
    private String next;
    private String title;
    private String answerType;
    private boolean traverse;

    public KGraph() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public boolean isTraverse() {
        return traverse;
    }

    public void setTraverse(boolean traverse) {
        this.traverse = traverse;
    }
}
