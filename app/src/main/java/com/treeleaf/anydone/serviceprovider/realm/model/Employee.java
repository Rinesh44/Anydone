package com.treeleaf.anydone.serviceprovider.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Employee extends RealmObject {
    public static final String EMPLOYEE_ID = "employeeId";
    @PrimaryKey
    private String employeeId;
    private String accountId;
    private long createdAt;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
}
