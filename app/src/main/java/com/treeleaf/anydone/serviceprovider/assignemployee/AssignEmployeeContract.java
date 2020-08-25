package com.treeleaf.anydone.serviceprovider.assignemployee;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

import java.util.List;

public class AssignEmployeeContract {
    public interface AssignEmployeeView extends BaseView {
        void assignEmployeeSuccess();

        void assignEmployeeFail(String msg);

        void getEmployeesSuccess(List<Employee> assignEmployeeList);

        void getEmployeesFail(String msg);
    }

    public interface AssignEmployeePresenter extends Presenter<AssignEmployeeView> {
        void assignEmployee(long ticketId, List<Employee> employeeList);

        void getEmployees();
    }
}
