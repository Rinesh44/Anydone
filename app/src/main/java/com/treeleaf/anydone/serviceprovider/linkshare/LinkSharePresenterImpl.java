package com.treeleaf.anydone.serviceprovider.linkshare;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LinkSharePresenterImpl extends BasePresenter<LinkShareContract.LinkShareView>
        implements LinkShareContract.LinkSharePresenter {
    private static final String TAG = "LinkSharePresenterImpl";

    private LinkShareRepository linkShareRepository;

    @Inject
    public LinkSharePresenterImpl(LinkShareRepository linkShareRepository) {
        this.linkShareRepository = linkShareRepository;
    }

    @Override
    public void getShareLink(String ticketId, String emailPhone) {
        getView().showProgressBar("please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);

        TicketProto.GetSharableLinkRequest getSharableLinkRequest =
                TicketProto.GetSharableLinkRequest.newBuilder()
                        .setTicketId(ticketId)
                        .setEmailOrPhone(emailPhone)
                        .build();

        ticketBaseResponseObservable = linkShareRepository.getShareLink(token,
                getSharableLinkRequest);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       ticketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get link response: "
                                        + ticketBaseResponse);

                                getView().hideProgressBar();
                                if (ticketBaseResponse == null) {
                                    getView().onLinkShareFail(
                                            "Link share failed");
                                    return;
                                }

                                if (ticketBaseResponse.getError()) {
                                    getView().onLinkShareFail(
                                            ticketBaseResponse.getMsg());
                                    return;
                                }

                                getView().onLinkShareSuccess(ticketBaseResponse.getLink());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onLinkShareFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }
}
