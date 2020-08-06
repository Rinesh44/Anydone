package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tickets extends RealmObject {
    @PrimaryKey
    long ticketId;
    String title;
    String description;
    Customer customer;
    ServiceProvider serviceProvider;
    String ticketSource;
    RealmList<Tags> tagsRealmList;
    String serviceId;

    public Tickets() {
    }

    public Tickets(long ticketId) {
        this.ticketId = ticketId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getTicketSource() {
        return ticketSource;
    }

    public void setTicketSource(String ticketSource) {
        this.ticketSource = ticketSource;
    }

    public RealmList<Tags> getTagsRealmList() {
        return tagsRealmList;
    }

    public void setTagsRealmList(RealmList<Tags> tagsRealmList) {
        this.tagsRealmList = tagsRealmList;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
