package com.treeleaf.anydone.serviceprovider.addticket;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordContract;

import java.util.List;

public class AddTicketContract {
    public interface ChangePasswordView extends BaseView {
        void showInvalidOldPasswordError();
    }

    public interface ChangePasswordPresenter extends Presenter<ChangePasswordContract.ChangePasswordView> {
        void createTicket(String title, String description, String customerId, List<String> tags,
                          String serviceId, List<String> assignedEmployeeIds);
    }
}
