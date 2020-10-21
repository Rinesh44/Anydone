package com.treeleaf.anydone.serviceprovider.contributed;


import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.tickets.contributedtickets.ContributedTicketRepository;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ContributedTicketPresenterImpl extends BasePresenter
        <com.treeleaf.anydone.serviceprovider.contributed.ContributedTicketContract.ContributedTicketView>
        implements ContributedTicketContract.ContributedTicketPresenter {

    private static final String TAG = "ContributedTicketPresen";
    private ContributedTicketRepository contributedTicketRepository;

    @Inject
    public ContributedTicketPresenterImpl(ContributedTicketRepository contributedTicketRepository) {
        this.contributedTicketRepository = contributedTicketRepository;
    }

    private void saveContributedTickets(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> contributedTickets = TicketRepo.getInstance().getContributedTickets();
        if (!CollectionUtils.isEmpty(contributedTickets)) {
            TicketRepo.getInstance().deleteContributedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete contributed tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.CONTRIBUTED,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getContributedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getContributedTicketFail("failed to save contributed tickets");
                        GlobalUtils.showLog(TAG, "failed to save contributed tickets");
                    }
                });
    }

    @Override
    public void getContributedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getContributedTickets(token,
                serviceId, from, to, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get contributed tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getContributedTicketFail("Get contributed" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getContributedTicketFail
                                            (getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "contributed tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                saveContributedTickets(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getContributedTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }
}

