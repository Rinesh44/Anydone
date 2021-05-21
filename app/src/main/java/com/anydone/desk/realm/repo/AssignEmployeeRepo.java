package com.anydone.desk.realm.repo;

import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Employee;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class AssignEmployeeRepo extends Repo {
    private static final AssignEmployeeRepo ASSIGN_EMPLOYEE_REPO;
    private static final String TAG = "AssignEmployeeRepo";

    static {
        ASSIGN_EMPLOYEE_REPO = new AssignEmployeeRepo();
    }

    public static AssignEmployeeRepo getInstance() {
        return ASSIGN_EMPLOYEE_REPO;
    }

    public void saveAssignEmployeeList(final List<UserProto.EmployeeProfile> employeeList,
                                       final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
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
            if (profile.getAccount().getAccountId() != null && !profile.getAccount().getAccountId().isEmpty()) {
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
        }
        return assignEmployeeList;
    }


    public List<AssignEmployee> getAllAssignEmployees() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            Employee self = EmployeeRepo.getInstance().getEmployee();
            if (self != null) {
                GlobalUtils.showLog(TAG, "check self emp id: " + self.getEmployeeId());
                return new ArrayList<>(realm.where(AssignEmployee.class)
                        .notEqualTo("assignEmployeeId", self.getEmployeeId())
                        .notEqualTo("accountId", "")
                        .findAll());
            } else {
                return new ArrayList<>(realm.where(AssignEmployee.class)
                        .findAll());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<AssignEmployee> searchEmployee(String query) {
        final Realm realm = Realm.getDefaultInstance();
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

    public AssignEmployee getAssignedEmployeeById(String employeeId) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(AssignEmployee.class)
                .equalTo("assignEmployeeId", employeeId)
                .findFirst();
    }

    public AssignEmployee getAssignedEmployeeByAccountId(String accountId) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(AssignEmployee.class)
                .equalTo("accountId", accountId)
                .findFirst();
    }

}
