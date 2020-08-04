package com.treeleaf.anydone.serviceprovider.servicerequests.ongoing;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OngoingPresenterImpl extends BasePresenter<OngoingRequestContract.OngoingView>
        implements OngoingRequestContract.OngoingPresenter {
    private static final String TAG = "OngoingPresenterImpl";
    private OngoingRepository ongoingRepository;

    @Inject
    public OngoingPresenterImpl(OngoingRepository ongoingRepository) {
        this.ongoingRepository = ongoingRepository;
    }

    @Override
    public void cancelOrder(String token, long orderId) {
        Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrderObservable;
        cancelOrderObservable = ongoingRepository.cancelOrder(token, orderId);

        getView().showProgressBar("Please wait...");
        addSubscription(cancelOrderObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith
                        (new DisposableObserver<OrderServiceRpcProto.OrderServiceBaseResponse>() {
                    @Override
                    public void onNext(
                            OrderServiceRpcProto.OrderServiceBaseResponse cancelOrderResponse) {
                        GlobalUtils.showLog(TAG, "cancel order response: "
                                + cancelOrderResponse);

                        getView().hideProgressBar();
                        if (cancelOrderResponse == null) {
                            getView().onOrderCancelFail("Logout failed");
                            return;
                        }

                        if (cancelOrderResponse.getError()) {
                            getView().onOrderCancelFail(cancelOrderResponse.getMsg());
                            return;
                        }

                        getView().onOrderCancelSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onOrderCancelFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }
}
