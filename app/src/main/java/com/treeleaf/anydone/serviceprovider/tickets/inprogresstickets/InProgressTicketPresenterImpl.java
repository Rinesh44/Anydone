package com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets;

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
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getInProgressTickets(token, serviceId, from, to, page);
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
