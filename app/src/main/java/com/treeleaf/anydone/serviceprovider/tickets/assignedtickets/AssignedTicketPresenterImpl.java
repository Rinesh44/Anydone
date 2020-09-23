package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

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

public class AssignedTicketPresenterImpl extends BasePresenter<AssignedTicketContract.AssignedTicketView>
        implements AssignedTicketContract.AssignedTicketPresenter {

    private static final String TAG = "AssignedTicketPresenter";
    private AssignedTicketRepository assignedTicketRepository;

    @Inject
    public AssignedTicketPresenterImpl(AssignedTicketRepository assignedTicketRepository) {
        this.assignedTicketRepository = assignedTicketRepository;
    }

    @Override
    public void getAssignedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = assignedTicketRepository
                .getAssignedTickets(token, serviceId, from, to, page);
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
                                    getView().getAssignedTicketFail("Get tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getAssignedTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id check: " + serviceId);
                                GlobalUtils.showLog(TAG, "assigned ticket count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                saveAssignedTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getAssignedTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }


    private void saveAssignedTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> assignedTickets = TicketRepo.getInstance().getAssignedTickets();
        if (CollectionUtils.isEmpty(assignedTickets)) {
            saveTickets(ticketsList);
        } else {
            TicketRepo.getInstance().deleteAssignedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "deleted all assigned tickets");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete assigned tickets");
                }
            });

            saveTickets(ticketsList);
        }
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.ASSIGNED, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getAssignedTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save assigned tickets");
                getView().getAssignedTicketSuccess();
            }
        });
    }
}
