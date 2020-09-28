package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

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

public class ClosedTicketPresenterImpl extends BasePresenter<ClosedTicketContract.ClosedTicketView>
        implements ClosedTicketContract.ClosedTicketPresenter {

    private static final String TAG = "ClosedTicketPresenterIm";
    private ClosedTicketRepository closedTicketRepository;

    @Inject
    public ClosedTicketPresenterImpl(ClosedTicketRepository closedTicketRepository) {
        this.closedTicketRepository = closedTicketRepository;
    }

    @Override
    public void getClosedResolvedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = closedTicketRepository.getClosedResolvedTickets(token, serviceId, from, to, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get closed tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getClosedTicketFail("Get closed tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getClosedTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }


                                GlobalUtils.showLog(TAG, "serivce id: " + serviceId);
                                GlobalUtils.showLog(TAG, "closed tickets Count: " + getTicketsBaseResponse.getTicketsList().size());
                                saveClosedTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getClosedTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );

    }

    @Override
    public void reopenTicket(long ticketId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = closedTicketRepository.reopenTicket(token, ticketId);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "reopen tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().onReopenFail("reopen ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().onReopenFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().onReopenSuccess(ticketId);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onReopenFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }


    private void saveClosedTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> closedResolvedTickets = TicketRepo.getInstance().getClosedResolvedTickets();
        if (!CollectionUtils.isEmpty(closedResolvedTickets)) {
            TicketRepo.getInstance().deleteClosedResolvedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete closed resolved tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.CLOSED_RESOLVED,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getClosedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getClosedTicketFail("failed to save closed tickets");
                        GlobalUtils.showLog(TAG, "failed to save closed tickets");
                    }
                });
    }
}
