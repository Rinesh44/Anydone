package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class AssignEmployeeRepo extends Repo {
    private static final AssignEmployeeRepo ASSIGN_EMPLOYEE_REPO;

    static {
        ASSIGN_EMPLOYEE_REPO = new AssignEmployeeRepo();
    }

    public static AssignEmployeeRepo getInstance() {
        return ASSIGN_EMPLOYEE_REPO;
    }

    public void saveAssignEmployeeList(final List<UserProto.EmployeeProfile> employeeList, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformAssignEmployee(employeeList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<AssignEmployee> transformAssignEmployee(List<UserProto.EmployeeProfile> employeeList) {
        List<AssignEmployee> assignEmployeeList = new ArrayList<>();
        for (UserProto.EmployeeProfile profile : employeeList
        ) {
            AssignEmployee employee = new AssignEmployee();
            employee.setAccountId(profile.getAccount().getAccountId());
            employee.setCreatedAt(profile.getCreatedAt());
            employee.setEmail(profile.getAccount().getEmail());
            employee.setEmployeeId(profile.getEmployeeProfileId());
            employee.setEmployeeImageUrl(profile.getAccount().getProfilePic());
            employee.setName(profile.getAccount().getFullName());
            employee.setPhone(profile.getAccount().getPhone());
            assignEmployeeList.add(employee);
        }
        return assignEmployeeList;
    }


    public List<AssignEmployee> getAllAssignEmployees() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(AssignEmployee.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<AssignEmployee> searchEmployee(String query) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            RealmQuery<AssignEmployee> result = performSearch(query, realm);
            return result.findAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    private RealmQuery<AssignEmployee> performSearch(String searchTerm, Realm realm) {
        RealmQuery<AssignEmployee> query = realm.where(AssignEmployee.class);
        query = query
                .contains("name", searchTerm, Case.INSENSITIVE);
        return query;
    }

}
