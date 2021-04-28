package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getAssignedTickets(token,
                serviceId, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get assigned tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getAssignedTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "assigned tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (getTicketsBaseResponse.getTicketsList().size() > 0)
                                    saveAssignedTickets(getTicketsBaseResponse.getTicketsList());
                                else getView().getAssignedTicketFail("No tickets found");

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
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

    @Override
    public void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority, AssignEmployee selectedEmp, TicketCategory selectedTicketType, Tags selectedTeam, Service selectedService) {

    }


    private void saveAssignedTickets(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> assignedTickets = TicketRepo.getInstance().getAssignedTickets();
        if (!CollectionUtils.isEmpty(assignedTickets)) {
            TicketRepo.getInstance().deleteAssignedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete assigned tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.OPEN,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getAssignedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getAssignedTicketFail("Tickets not found");
                        GlobalUtils.showLog(TAG, "failed to save assigned tickets");
                    }
                });
    }
}
