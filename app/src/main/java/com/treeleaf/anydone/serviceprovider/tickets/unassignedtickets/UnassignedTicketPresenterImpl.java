package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
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

public class UnassignedTicketPresenterImpl extends BasePresenter<UnassignedTicketsContract.UnassignedView>
        implements UnassignedTicketsContract.UnassignedPresenter {

    private static final String TAG = "UnassignedTicketPresent";
    private UnassignedTicketRepository unassignedTicketRepository;

    @Inject
    public UnassignedTicketPresenterImpl(UnassignedTicketRepository unassignedTicketRepository) {
        this.unassignedTicketRepository = unassignedTicketRepository;
    }

    @Override
    public void getAssignableTickets(long from, long to, int pageSize) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = unassignedTicketRepository.getAssignableTickets(token, from, to, pageSize);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get assignable tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getAssignableTicketFail("Get assignable tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getAssignableTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                saveAssignableTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getAssignableTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void assignTicket(long ticketId, String employeeId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        String token = Hawk.get(Constants.TOKEN);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(String.valueOf(employeeId))
                .build();
        TicketProto.EmployeeAssigned employeeAssigned = TicketProto.EmployeeAssigned.newBuilder()
                .setAssignedTo(employeeProfile)
                .setAssignedAt(System.currentTimeMillis())
                .build();

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addEmployeesAssigned(employeeAssigned)
                .build();

        GlobalUtils.showLog(TAG, "employee assinged check:" + employeeAssigned);

        getTicketsObservable = unassignedTicketRepository.assignTicket(token, ticketId, ticket);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "assign tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().assignFail("assign ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().assignFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().assignSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().assignFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveAssignableTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.ASSIGNABLE, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getAssignableTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save assignable tickets");
                getView().getAssignableTicketFail("Failed to save assignable tickets");
            }
        });
    }
}
