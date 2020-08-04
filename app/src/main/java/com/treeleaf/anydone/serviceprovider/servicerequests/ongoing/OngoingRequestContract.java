package com.treeleaf.anydone.serviceprovider.servicerequests.ongoing;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class OngoingRequestContract {
    public interface OngoingView extends BaseView {
        void onOrderCancelSuccess();

        void onOrderCancelFail(String msg);
    }

    public interface OngoingPresenter extends Presenter<OngoingView> {
        void cancelOrder(String token, long orderId);
    }
}
