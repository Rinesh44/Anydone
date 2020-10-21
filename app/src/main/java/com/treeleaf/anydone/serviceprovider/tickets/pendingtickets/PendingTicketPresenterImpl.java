package com.treeleaf.anydone.serviceprovider.tickets.pendingtickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PendingTicketPresenterImpl extends BasePresenter<PendingTicketContract.PendingTicketView>
        implements PendingTicketContract.PendingTicketPresenter {

    private static final String TAG = "PendingTicketPresenterI";
    private PendingTicketRepository pendingTicketRepository;

    @Inject
    public PendingTicketPresenterImpl(PendingTicketRepository pendingTicketRepository) {
        this.pendingTicketRepository = pendingTicketRepository;
    }

    @Override
    public void getPendingTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        getTicketsObservable = service
                .getPendingTickets(token, serviceId, from, to, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getPendingTicketFail("Get tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getPendingTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id check: " + serviceId);
                                GlobalUtils.showLog(TAG, "pending ticket count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (CollectionUtils.isEmpty(getTicketsBaseResponse.getTicketsList())) {
                                    getView().showEmptyView();
                                } else
                                    savePendingTicketsToRealm(getTicketsBaseResponse.getTicketsList());

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getPendingTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }


    private void savePendingTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> pendingTickets = TicketRepo.getInstance().getPendingTickets();
        if (CollectionUtils.isEmpty(pendingTickets)) {
            saveTickets(ticketsList);
        } else {
            TicketRepo.getInstance().deletePendingTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "deleted all pending tickets");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete pending tickets");
                }
            });

            saveTickets(ticketsList);
        }
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.PENDING,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getPendingTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "failed to save pending tickets");
                        getView().getPendingTicketSuccess();
                    }
                });
    }
}
