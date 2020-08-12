package com.treeleaf.anydone.serviceprovider.servicerequests.accepted;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class AcceptedRequestContract {
    public interface OngoingView extends BaseView {
        void onOrderCancelSuccess();

        void onOrderCancelFail(String msg);
    }

    public interface OngoingPresenter extends Presenter<OngoingView> {
        void cancelOrder(String token, long orderId);
    }
}
