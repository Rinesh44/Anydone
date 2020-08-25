package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

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

        getTicketsObservable = closedTicketRepository.getClosedResolvedTickets(token, from, to, page);
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

                                getView().onReopenSuccess();
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
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.CLOSED_RESOLVED, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getClosedTicketSuccess();
            }

            @Override
            public void fail() {
                getView().getClosedTicketSuccess();
                GlobalUtils.showLog(TAG, "failed to save closed tickets");
            }
        });
    }
}
