package com.anydone.desk.moretickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class MoreTicketContract {

    public interface MoreTicketView extends BaseView {
        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void getTicketTypeSuccess();

        void getTicketTypeFail(String msg);

        void getTeamSuccess();

        void getTeamFail(String msg);

        void getServicesSuccess();

        void getServicesFail(String msg);
    }

    public interface MoreTicketPresenter extends Presenter<MoreTicketContract.MoreTicketView> {

        void findEmployees();

        void findTicketTypes();

        void findTeams();

        void getServices();
    }
}
