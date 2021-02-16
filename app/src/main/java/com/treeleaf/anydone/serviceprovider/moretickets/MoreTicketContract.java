package com.treeleaf.anydone.serviceprovider.moretickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

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
