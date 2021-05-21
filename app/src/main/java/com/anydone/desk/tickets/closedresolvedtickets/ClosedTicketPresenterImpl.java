package com.anydone.desk.tickets.closedresolvedtickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
        if (showProgress && getView() != null) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getClosedResolvedTickets(token, serviceId, from, to, 100,
                "DESC");
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
                                GlobalUtils.showLog(TAG, "closed tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (!CollectionUtils.isEmpty(getTicketsBaseResponse.getTicketsList())) {
                                    saveClosedTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                                } else {
                                    getView().showEmptyView();
                                }
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        getTicketsObservable = service.reopenTicket(token, ticketId, "reopen ticket");
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
        if (CollectionUtils.isEmpty(closedResolvedTickets)) {
            saveTickets(ticketsList);
        } else {
            TicketRepo.getInstance().deleteClosedResolvedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "deleted all resolved tickets");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete closed resolved tickets");
                }
            });

            saveTickets(ticketsList);
        }
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
