package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
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

public class SubscribeTicketPresenterImpl extends BasePresenter<SubscribeTicketContract.SubscribeTicketsView>
        implements SubscribeTicketContract.SubscribeTicketsPresenter {

    private static final String TAG = "SubscribeTicketPresente";
    private SubscribeTicketRepository subscribeTicketRepository;


    @Inject
    public SubscribeTicketPresenterImpl(SubscribeTicketRepository subscribeTicketRepository) {
        this.subscribeTicketRepository = subscribeTicketRepository;
    }

    @Override
    public void getSubscribedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = subscribeTicketRepository.getSubscribedTickets(token, serviceId, from, to, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get subscribed tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getSubscribedTicketsFail("Get subscribed tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getSubscribedTicketsFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service Id: " + serviceId);
                                GlobalUtils.showLog(TAG, "subscribed ticket count: " + getTicketsBaseResponse.getTicketsList().size());
                                saveSubscribedTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getSubscribedTicketsFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void unsubscribeTicket(long ticketId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = subscribeTicketRepository.unsubscribe(token, ticketId);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "unsubscribe tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().onUnsubscribeFail("unsubscribe ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().onUnsubscribeFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().onUnsubscribeSuccess(ticketId);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onUnsubscribeFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveSubscribedTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
        if (CollectionUtils.isEmpty(subscribedTickets)) {
            saveTickets(ticketsList);
        } else {

            TicketRepo.getInstance().deleteSubscribedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {

                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete subscribed tickets");
                }
            });

            saveTickets(ticketsList);
        }
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.SUBSCRIBED,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getSubscribedTicketsSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getSubscribedTicketsSuccess();
                        GlobalUtils.showLog(TAG, "failed to save subscribed tickets");
                    }
                });
    }

}
