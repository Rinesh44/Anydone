package com.anydone.desk.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tickets extends RealmObject {
    @PrimaryKey
    String id;
    long ticketId;
    long ticketIndex;
    String threadId;
    String title;
    String description;
    String ticketCategory;
    String ticketCategoryId;
    Customer customer;
    ServiceProvider serviceProvider;
    String ticketSource;
    RealmList<Tags> tagsRealmList;
    RealmList<Label> labelRealmList;
    RealmList<AssignEmployee> contributorList;
    String serviceId;
    String estimatedTime;
    long estimatedTimeStamp;
    String customerType;
    String ticketType;
    long createdAt;
    String ticketStatus;
    String createdByName;
    String createdByPic;
    String createdById;
    AssignEmployee assignedEmployee;
    int priority;
    boolean botEnabled;
    DependentTicket dependentTicket;
    RealmList<Attachment> attachmentList;

    public Tickets() {
    }

    public long getTicketIndex() {
        return ticketIndex;
    }

    public void setTicketIndex(long ticketIndex) {
        this.ticketIndex = ticketIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByPic() {
        return createdByPic;
    }

    public void setCreatedByPic(String createdByPic) {
        this.createdByPic = createdByPic;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public AssignEmployee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(AssignEmployee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public RealmList<AssignEmployee> getContributorList() {
        return contributorList;
    }

    public void setContributorList(RealmList<AssignEmployee> contributorList) {
        this.contributorList = contributorList;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public void setBotEnabled(boolean botEnabled) {
        this.botEnabled = botEnabled;
    }

    public String getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(String ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public RealmList<Label> getLabelRealmList() {
        return labelRealmList;
    }

    public void setLabelRealmList(RealmList<Label> labelRealmList) {
        this.labelRealmList = labelRealmList;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public long getEstimatedTimeStamp() {
        return estimatedTimeStamp;
    }

    public void setEstimatedTimeStamp(long estimatedTimeStamp) {
        this.estimatedTimeStamp = estimatedTimeStamp;
    }

    public String getTicketCategoryId() {
        return ticketCategoryId;
    }

    public void setTicketCategoryId(String ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public DependentTicket getDependentTicket() {
        return dependentTicket;
    }

    public void setDependentTicket(DependentTicket dependentTicket) {
        this.dependentTicket = dependentTicket;
    }

    public RealmList<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(RealmList<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
