package com.anydone.desk.dashboard;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class DashboardContract {
    public interface DashboardView extends BaseView {
        void getServicesSuccess();

        void getServicesFail(String msg);

        void getTicketByDateSuccess();

        void getTicketByDateFail(String msg);

        void getTicketByStatusSuccess();

        void getTicketByStatusFail(String msg);

        void getTicketByPrioritySuccess();

        void getTicketByPriorityFail(String msg);

        void getTicketBySourceSuccess();

        void getTicketBySourceFail(String msg);

        void getTicketByResolvedTimeSuccess();

        void getTicketByResolvedTimeFail(String msg);

        void onFilterByDateFail(String msg);

        void onFilterByStatusFail(String msg);

        void onFilterByPriorityFail(String msg);

        void onFilterBySourceFail(String msg);

        void onFilterByResolvedTimeFail(String msg);

    }

    public interface DashboardPresenter extends Presenter<DashboardContract.DashboardView> {
        void getServices();

        void getTicketsByDate();

        void getTicketByStatus();

        void getTicketByPriority();

        void getTicketBySource();

        void getTicketByResolveTime();

        void filterByDate(long from, long to);

        void filterBySource(long from, long to);

        void filterByPriority(long from, long to);

        void filterByStatus(long from, long to);

        void filterByResolvedTime(long from, long to);

    }
}
