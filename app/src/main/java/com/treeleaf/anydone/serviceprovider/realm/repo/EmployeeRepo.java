package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import io.realm.Realm;

public class EmployeeRepo extends Repo {
    private static final EmployeeRepo EMPLOYEE_REPO;

    static {
        EMPLOYEE_REPO = new EmployeeRepo();
    }


    public static EmployeeRepo getInstance() {
        return EMPLOYEE_REPO;
    }

    public void saveEmployee(final AuthProto.LoginResponse loginResponse, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Employee employee = setEmployee(loginResponse.getUser().getEmployee(), realm1);
                realm1.copyToRealmOrUpdate(employee);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private Employee setEmployee(UserProto.EmployeeProfile employeeProfilePb, Realm realm) {
        Employee employee = realm.where(Employee.class)
                .equalTo(Employee.EMPLOYEE_ID, employeeProfilePb.getEmployeeProfileId())
                .findFirst();
        if (employee != null) return employee;
        return transformEmployee(realm.createObject(Employee.class,
                employeeProfilePb.getEmployeeProfileId()), employeeProfilePb);
    }

    private Employee transformEmployee(Employee employee,
                                       UserProto.EmployeeProfile employeeProfile) {
        employee.setAccountId(employeeProfile.getAccount().getAccountId());
        employee.setName(employeeProfile.getAccount().getFullName());
        employee.setEmployeeImageUrl(employeeProfile.getAccount().getProfilePic());
        employee.setCreatedAt(employeeProfile.getCreatedAt());
        employee.setPhone(employeeProfile.getAccount().getPhone());
        employee.setEmail(employeeProfile.getAccount().getEmail());
        return employee;
    }

    public Employee getEmployeeByAccountId(String accountId) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(Employee.class)
                .equalTo("accountId", accountId)
                .findFirst();
    }

    public Employee getEmployeeById(String employeeId) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(Employee.class)
                .equalTo("employeeId", employeeId)
                .findFirst();
    }

    public Employee getEmployee() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Employee.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
