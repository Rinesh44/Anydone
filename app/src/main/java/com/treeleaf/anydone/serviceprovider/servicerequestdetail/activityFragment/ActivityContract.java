package com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;

public class ActivityContract {

    public interface ActivityView extends BaseView {
        void setRequestValues(String requesterName, String requesterImage, String date,
                              String location, String requestedDate, String requestedTime,
                              String serviceStatus, ServiceProvider serviceProvider,
                              long startedAt, long completedAt, long acceptedAt,
                              long closedAt);

        void getRequestFailed(String msg);
    }

    public interface ActivityPresenter extends Presenter<ActivityContract.ActivityView> {

        void getRequest(long requestId);
    }
}
