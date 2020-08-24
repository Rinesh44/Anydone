package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssignEmployee extends RealmObject {
    public static final String ASSIGN_EMPLOYEE_ID = "assignEmployeeId";
    @PrimaryKey
    private String assignEmployeeId;
    private String accountId;
    private long createdAt;
    private String employeeImageUrl;
    private String email;
    private String phone;
    private String name;

    public AssignEmployee() {
    }

    public String getEmployeeId() {
        return assignEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.assignEmployeeId = employeeId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
