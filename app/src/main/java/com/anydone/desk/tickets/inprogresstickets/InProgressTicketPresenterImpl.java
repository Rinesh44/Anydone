package com.anydone.desk.tickets.inprogresstickets;

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
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class InProgressTicketPresenterImpl extends BasePresenter<InProgressTicketContract.InProgressTicketsView>
        implements InProgressTicketContract.InProgressTicketsPresenter {

    private static final String TAG = "InProgressTicketPresent";
    private InProgressTicketRepository inProgressTicketRepository;


    @Inject
    public InProgressTicketPresenterImpl(InProgressTicketRepository inProgressTicketRepository) {
        this.inProgressTicketRepository = inProgressTicketRepository;
    }

    @Override
    public void getInProgressTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress && getView() != null) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getInProgressTickets(token, serviceId, from, to, page, "ASC");
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get inprogress tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getInProgressTicketsFail
                                            ("Get in-progress tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getInProgressTicketsFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service Id: " + serviceId);
                                GlobalUtils.showLog(TAG, "in progress ticket count: " +
                                        getTicketsBaseResponse.getTicketsList().size());

                                if (CollectionUtils.isEmpty(getTicketsBaseResponse.getTicketsList())) {
                                    getView().showEmptyView();
                                } else
                                    saveInProgressTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getInProgressTicketsFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void closeTicket(long ticketId) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);

        ticketObservable = service.closeTicket(token,
                ticketId, "");
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                               closeTicketResponse) {
                        GlobalUtils.showLog(TAG, "close ticket response: " +
                                closeTicketResponse);

                        getView().hideProgressBar();
                        if (closeTicketResponse == null) {
                            getView().onCloseTicketFail("Failed to close ticket");
                            return;
                        }

                        if (closeTicketResponse.getError()) {
                            getView().onCloseTicketFail(closeTicketResponse.getMsg());
                            return;
                        }

                        getView().onCloseTicketSuccess(ticketId);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void resolveTicket(long ticketId) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);

        ticketObservable = service.resolveTicket(token,
                Long.parseLong(String.valueOf(ticketId)));
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                               response) {
                        GlobalUtils.showLog(TAG, "start ticket response: " +
                                response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onResolveTicketFail("Failed to resolve ticket");
                            return;
                        }

                        if (response.getError()) {
                            getView().onResolveTicketFail(response.getMsg());
                            return;
                        }

                        getView().onResolveTicketSuccess(ticketId);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    private void saveInProgressTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
        if (CollectionUtils.isEmpty(inProgressTickets)) {
            saveTickets(ticketsList);
        } else {

            TicketRepo.getInstance().deleteInProgressTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "in progress tickets deleted");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete in progress tickets");
                }
            });

            saveTickets(ticketsList);
        }
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.IN_PROGRESS,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getInProgressTicketsSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getInProgressTicketsFail("Failed");
                        GlobalUtils.showLog(TAG, "failed to save in-progress tickets");
                    }
                });
    }

}
