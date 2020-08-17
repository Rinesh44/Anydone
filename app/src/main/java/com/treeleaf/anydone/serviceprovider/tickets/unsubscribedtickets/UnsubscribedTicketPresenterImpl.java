package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UnsubscribedTicketPresenterImpl extends BasePresenter<UnsubscribedTicketContract.UnsubscribedView>
        implements UnsubscribedTicketContract.UnsubscribedPresenter {

    private static final String TAG = "UnsubscribedTicketPrese";
    private UnsubscribedTicketRepository unsubscribedTicketRepository;

    @Inject
    public UnsubscribedTicketPresenterImpl(UnsubscribedTicketRepository unsubscribedTicketRepository) {
        this.unsubscribedTicketRepository = unsubscribedTicketRepository;
    }

    @Override
    public void getSubscribeableTickets(long from, long to, int pageSize) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = unsubscribedTicketRepository.getSubscribeableTickets(token, from, to, pageSize);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get subscribeable tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getSubscribeableTicketFail("Get subscribeable tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getSubscribeableTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                saveSubscribeableTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getSubscribeableTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void subscribe(long ticketId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = unsubscribedTicketRepository.subscribe(token, ticketId);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "subscribe tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().onSubscribeFail("subscribe ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().onSubscribeFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().onSubscribeSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onSubscribeFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveSubscribeableTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.SUBSCRIBEABLE, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getSubscribeableTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save subscribeable tickets");
                getView().getSubscribeableTicketFail("Failed to save subscribeable tickets");
            }
        });
    }
}
