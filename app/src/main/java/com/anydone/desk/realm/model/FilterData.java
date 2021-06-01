package com.anydone.desk.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FilterData extends RealmObject {

    @PrimaryKey
    String serviceId;
    String searchQuery;
    long from;
    long to;
    int ticketState;
    int priority;
    AssignEmployee assignEmployee;
    TicketCategory ticketCategory;
    Tags tags;
    Service service;
    Customer customer;

    public FilterData() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getTicketState() {
        return ticketState;
    }

    public void setTicketState(int ticketState) {
        this.ticketState = ticketState;
    }

    public AssignEmployee getAssignEmployee() {
        return assignEmployee;
    }

    public void setAssignEmployee(AssignEmployee assignEmployee) {
        this.assignEmployee = assignEmployee;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
